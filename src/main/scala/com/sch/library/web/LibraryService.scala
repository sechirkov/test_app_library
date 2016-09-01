package com.sch.library.web

import akka.actor.Actor
import com.sch.library.domain.Library
import spray.http.MediaType
import spray.http.MediaTypes._
import spray.routing._

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

  lazy val library = Library.library

  val username = "User User"

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
      path("books") {
        get {
          respondWithMediaType(`text/html`) {
            complete {
              <html>
                <body>
                  <p>List of available books:</p>
                  <table>
                    <thead>
                      <tr>
                        <th>Name</th>
                        <th>Authors</th>
                      </tr>
                    </thead>
                    <tbody>
                      {library.books map (b => <tr>
                      <td>
                        {b.book.name}
                      </td> <td>
                        {b.book.authors mkString ", "}
                      </td>
                    </tr>)}
                    </tbody>
                  </table>
                </body>
              </html>
            }
          }
        }
      }
}