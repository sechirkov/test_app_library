package com.sch.library.web

import java.sql.Timestamp
import java.util.Date

import akka.actor.Actor
import com.sch.library.domain.{Book, LogBook, User}
import com.sch.library.service.ComponentRegistry
import com.sch.library.util.InventoryNumberGenerator
import com.sch.library.web.model.{BookListJson, FailedJson, TakeBookJson, UserListJson}
import spray.http.HttpCookie
import spray.httpx.SprayJsonSupport._
import spray.routing._

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.language.postfixOps
import scala.util.{Failure, Success}

// we don't implement our route structure directly in the service actor because
// we want to be able to test it independently, without having to spin up an actor
class LibraryServiceActor extends Actor with LibraryService {

  // the HttpService trait defines only one abstract member, which
  // connects the services environment to the enclosing actor or test
  def actorRefFactory = context

  // this actor only runs our route, but you could add
  // other things here, like request stream processing
  // or timeout handling
  def receive = runRoute(myRoute)
}

// this trait defines our service behavior independently from the service actor
trait LibraryService extends HttpService {

  val bookService = ComponentRegistry.bookService
  val userService = ComponentRegistry.userService
  val logbookService = ComponentRegistry.logbookService

  val currentUser = Await.result(userService.findByLogin("user"), 1 second)
  val admin = Await.result(userService.findByLogin("admin"), 1 second)

  val myRoute =
    pathPrefix("css") {
      get {
        getFromResourceDirectory("css")
      }
    } ~
      pathPrefix("js") {
        get {
          getFromResourceDirectory("js")
        }
      } ~
      path("index.html") {
        getFromResource("views/index.html")
      } ~
      path("books.html") {
        getFromResource("views/books.html")
      } ~
      path("books.json") {
        post {
          onComplete(bookService.findAvailable()) {
            case Success(books) => complete(BookListJson(data = books))
            case Failure(ex) => complete(FailedJson(s"Cannot load data: ${ex.getMessage}"))
          }
        }
      } ~
      path("save-book") {
        post {
          entity(as[Book]) { book => {
            onComplete(bookService.persist(book.copy(inventoryNumber = Some(InventoryNumberGenerator.generate(book.title, book.authors, new Date()))))) {
              case Success(b) => complete(b)
              case Failure(ex) => failWith(ex)
            }
          }
          }
        }
      } ~
      path("take-book") {
        post {
          entity(as[TakeBookJson]) {
            (request) => {
              onComplete(logbookService.persist(LogBook(None, request.bookId, currentUser.get.id.get, admin.get.id.get, new Timestamp(System.currentTimeMillis)))) {
                case Success(lb) => complete(lb.id.get.toString)
                case Failure(ex) => failWith(ex)
              }
            }
          }
        }
      } ~
      path("users.html") {
        getFromResource("views/users.html")
      } ~
      path("users.json") {
        post {
          onComplete(userService.findAll()) {
            case Success(users) => complete(UserListJson(data = users))
            case Failure(ex) => complete(FailedJson(s"Cannot load data: ${ex.getMessage}"))
          }
        }
      } ~
      path("add-user.html") {
        getFromResource("views/add-user.html")
      } ~
      path("add-book.html") {
        getFromResource("views/add-book.html")
      } ~
      path("save-user") {
        post {
          entity(as[User]) { user => {
            onComplete(userService.persist(user)) {
              case Success(u) => complete(u)
              case Failure(ex) => failWith(ex)
            }
          }
          }
        }
      } /*~
      optionalCookie("currentUser") {
        case Some(nameCookie) => complete(s"The logged in user is '${nameCookie.content}'")
        case None => complete("No user logged in")
      } ~
      setCookie(HttpCookie("userName", content = currentUser.get.login)) {
        complete("The user was logged in")
      }*/
}