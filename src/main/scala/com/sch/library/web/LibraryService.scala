package com.sch.library.web

import java.sql.Timestamp
import java.util.Date

import akka.actor.Actor
import com.sch.library.domain.{Book, LogBook, User}
import com.sch.library.util.InventoryNumberGenerator
import com.sch.library.web.model._
import spray.caching.{Cache, LruCache}
import spray.http.HttpCookie
import spray.httpx.SprayJsonSupport._
import spray.routing._
import spray.routing.authentication._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration._
import scala.language.postfixOps
import scala.util.{Failure, Success}



class LibraryServiceActor extends Actor with StaticContentService with LibraryService with BasicAuthenticationService {

  // the HttpService trait defines only one abstract member, which
  // connects the services environment to the enclosing actor or test
  def actorRefFactory = context

  // this actor only runs our route, but you could add
  // other things here, like request stream processing
  // or timeout handling

  import com.sch.library.service.ComponentRegistry._
  def receive = runRoute(staticResources ~ authenticate(basicAuth(authenticator = new DaoUserPassAuthenticator()))(routes))
}

trait StaticContentService extends HttpService {
  val staticResources = pathPrefix("css" | "js" | "png") {
    get {
      getFromResourceDirectory("js")
    }
  }
}

trait BasicAuthenticationService {

  lazy val cache: Cache[Option[User]] = LruCache(timeToLive = 5 minute)

  def basicAuth(authenticator: UserPassAuthenticator[User],
                   realm: String = "secured resource"):BasicHttpAuthenticator[User] = BasicAuth(authenticator, realm)

  class DaoUserPassAuthenticator(cache: Cache[Option[User]] = cache)(implicit userService: UserService) extends UserPassAuthenticator[User] {
    def apply(userPass: Option[UserPass]): Future[Option[User]] = CachedUserPassAuthenticator(authenticator, cache = cache).apply(userPass)

    private def authenticator(userPass: Option[UserPass]): Future[Option[User]] = {
      (for {
        p <- userPass
      } yield userService.findByLogin(p.user)).getOrElse(Future.successful {
        None
      })
    }
  }
}

trait BookListService extends HttpService {

  def booksRoute(implicit bookService: BookService, userService: UserService, logbookService: LogBookService): SecureRoute = admin =>
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
      path("take-book") {
        post {
          entity(as[BookIdJson]) {
            request => {
              cookie(currentUserCookieName.value) { currentUser =>
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
      }
}

trait AddBookService extends HttpService {

  def addBookRoute(implicit bookService: BookService): SecureRoute = admin =>
    path("add-book.html") {
      getFromResource("views/add-book.html")
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
      }
}

trait UserListService extends HttpService {

  def usersRoute(implicit userService: UserService): SecureRoute = admin =>
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
      }
}

trait AddUserService extends HttpService {

  def addUserRoute(implicit userService: UserService): SecureRoute = admin =>
    path("add-user.html") {
      getFromResource("views/add-user.html")
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
      }
}

trait TakenBooksService extends HttpService {

  def takenBooksRoute(implicit bookService: BookService, userService: UserService, logbookService: LogBookService): SecureRoute = admin =>
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

trait UserSessionService extends HttpService {

  def userSessionRoute(implicit userService:UserService): SecureRoute = admin =>
    path("process-user") {
      post {
        entity(as[UserSessionJson]) { request =>
          val login = request.login.getOrElse("")
          val cookie = HttpCookie(currentUserCookieName.value, login)
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
    }
}

trait LibraryService extends HttpService
  with BookListService
  with AddBookService
  with UserListService
  with AddUserService
  with TakenBooksService
  with UserSessionService {

  private def routeList(implicit bookService: BookService, logbookService: LogBookService, userService: UserService) =
    List[SecureRoute](booksRoute, addBookRoute, usersRoute, addUserRoute, takenBooksRoute, userSessionRoute)

  val indexRoute = path("index.html") {
    getFromResource("views/index.html")
  }

  def routes(implicit bookService: BookService, logbookService: LogBookService, userService: UserService): SecureRoute = admin =>
    routeList.foldLeft(indexRoute) {
      (route, sr) => route ~ sr(admin)
    }
}