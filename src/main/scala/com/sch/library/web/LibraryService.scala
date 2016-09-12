package com.sch.library.web

import java.util.Date

import akka.actor.Actor
import com.sch.library.domain.Book
import com.sch.library.service.ComponentRegistry
import com.sch.library.util.InventoryNumberGenerator
import com.sch.library.web.model.{BookListJson, FailedJson}
import spray.http.FormData
import spray.http.MediaTypes._
import spray.httpx.SprayJsonSupport._
import spray.json._
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
          entity(as[String]) {
            (bookId) => {
              complete(bookId)
            }
          }
        }
      } ~
      path("users") {
        get {
          respondWithMediaType(`text/html`) {
            onComplete(userService.findAll()) {
              case Success(users) =>
                complete {
                  <html>
                    <body>
                      <p>List of users:</p>
                      <table>
                        <thead>
                          <tr>
                            <th>Login</th>
                            <th>First Name</th>
                            <th>Last Name</th>
                            <th>Second Name</th>
                          </tr>
                        </thead>
                        <tbody>
                          {users map (u =>
                          <tr>
                            <td>{u.login}</td>
                            <td>{u.firstName}</td>
                            <td>{u.lastName}</td>
                            <td>{u.secondName.getOrElse("")}</td>
                            <td>{u.status}</td>
                          </tr>)}
                        </tbody>
                      </table>
                    </body>
                  </html>
                }
              case Failure(ex) => complete {
                <html>
                  <p>Service is not available.</p>
                </html>
              }
            }
          }
        }
      }
}