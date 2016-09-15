package com.sch.library.web

import java.sql.Timestamp
import java.util.Date

import akka.actor.Actor
import com.sch.library.domain.{Book, LogBook, User}
import com.sch.library.service.{BookServiceComponent, ComponentRegistry, LogBookServiceComponent, UserServiceComponent}
import com.sch.library.util.InventoryNumberGenerator
import com.sch.library.web.model._
import spray.caching.{Cache, LruCache}
import spray.http.HttpCookie
import spray.httpx.SprayJsonSupport._
import spray.routing._
import spray.routing.authentication.{BasicAuth, BasicHttpAuthenticator, CachedUserPassAuthenticator, UserPass}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration._
import scala.language.postfixOps
import scala.util.{Failure, Success}

// we don't implement our route structure directly in the service actor because
// we want to be able to test it independently, without having to spin up an actor
class LibraryServiceActor extends {
  val userService = ComponentRegistry.userService
  val bookService = ComponentRegistry.bookService
  val logbookService = ComponentRegistry.logbookService
} with Actor with StaticContentService with LibraryService with BasicAuthenticationService {

  // the HttpService trait defines only one abstract member, which
  // connects the services environment to the enclosing actor or test
  def actorRefFactory = context

  // this actor only runs our route, but you could add
  // other things here, like request stream processing
  // or timeout handling
  def receive = runRoute(staticResources ~ authenticate(basicAuth())(route))
}

// this trait defines our service behavior independently from the service actor
trait StaticContentService extends HttpService {
  val staticResources = pathPrefix("css") {
    get {
      getFromResourceDirectory("css")
    }
  } ~
    pathPrefix("js") {
      get {
        getFromResourceDirectory("js")
      }
    }
}

trait BasicAuthenticationService extends HttpService {

  type UserPassAuthenticator[T] = Option[UserPass] => Future[Option[T]]

  val userService: UserServiceComponent#UserService

  lazy val cache:Cache[Option[User]] = LruCache(timeToLive = 5 minute)

  def basicAuth[T](authenticator: UserPassAuthenticator[T] = DaoUserPassAuthenticator,
                   realm: String = "secured resource"):BasicHttpAuthenticator[T] = BasicAuth(authenticator, realm)

  object DaoUserPassAuthenticator extends UserPassAuthenticator[User] {
    override def apply(userPass: Option[UserPass]): Future[Option[User]] = (for {
      p <- userPass
    } yield userService.findByLogin(p.user)).getOrElse(Future {
      None
    })
  }

  object CachedDaoUserPassAuthenticator extends UserPassAuthenticator[User] {
    override def apply(userPass: Option[UserPass]): Future[Option[User]] = CachedUserPassAuthenticator(DaoUserPassAuthenticator, cache = cache)(userPass)
  }


  class UserDaoAuthenticator extends





  def authenticator(userPass: Option[UserPass]): Future[Option[User]] =

  def basicAuth = authenticate(BasicAuth(CachedUserPassAuthenticator(authenticator, cache = cache) _, realm = "secured resource"))
}

trait LibraryService extends HttpService  {

  val bookService: BookServiceComponent#BookService
  val userService: UserServiceComponent#UserService
  val logbookService: LogBookServiceComponent#LogBookService

  def route: SecureRoute = admin =>
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
          entity(as[BookIdJson]) {
            request => {
              cookie("current_user") { currentUser =>
                onComplete(userService.findByLogin(currentUser.content) flatMap {
                  case Some(user) => logbookService.persist(LogBook(None, request.bookId, user.id.get, admin.id.get, new Timestamp(System.currentTimeMillis)))
                  case _ => throw new RuntimeException
                }) {
                  case Success(lb) => complete(lb.id.get.toString)
                  case Failure(ex) => failWith(ex)
                }
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
      } ~
      path("process-user") {
        post {
          entity(as[ProcessUserJson]) { request =>
            val login = request.login.getOrElse("")
            val cookie = HttpCookie("current_user", login)
            request.action match {
              case UserAction.startWork => onComplete(userService.findByLogin(login)) {
                case Success(Some(user)) =>
                  setCookie(cookie) {
                    complete(SuccessJson())
                  }
                case Success(None) => complete(FailedJson(s"User $login is not found."))
                case Failure(ex) => failWith(ex)
              }
              case UserAction.endWork =>
                deleteCookie(cookie) {
                  complete(SuccessJson())
                }
            }
          }
        }
      } ~
      path("taken-books.html") {
        getFromResource("views/taken-books.html")
      } ~
      path("taken-books.json") {
        post {
          cookie("current_user") { currentUser =>
            onComplete(userService.findByLogin(currentUser.content) flatMap {
              case Some(user) => bookService.findBooksTakenByUser(user.id.get)
              case None => throw new RuntimeException(s"User ${currentUser.content} is not found.")
            }) {
              case Success(books) => complete(BookListJson(data = books))
              case Failure(ex) => complete(FailedJson(s"Cannot load data: ${ex.getMessage}"))
            }
          }
        }
      } ~ path("return-book") {
      entity(as[BookIdJson]) { request =>
        onComplete(logbookService.findLastEntryByBookId(request.bookId) flatMap {
          case Some(logbook) => logbookService.returnBook(logbook.copy(receivedByUser = admin.id, returnDate = Some(new Timestamp(System.currentTimeMillis))))
          case None => throw new RuntimeException("Logbook is not found!")
        }) {
          case Success(_) => complete(SuccessJson())
          case Failure(ex) => failWith(ex)
        }
      }
    }
}