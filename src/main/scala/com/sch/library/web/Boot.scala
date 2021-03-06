package com.sch.library.web

import akka.actor.{ActorSystem, Props}
import akka.io.IO
import akka.pattern.ask
import akka.util.Timeout
import com.sch.library.dao.PostgresLibraryDatabase
import spray.can.Http

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.language.postfixOps

object Boot extends App {

  // we need an ActorSystem to host our application in
  implicit val system = ActorSystem("on-spray-can")

  // create and start our service actor
  val service = system.actorOf(Props[LibraryServiceActor], "library-service")

  implicit val timeout = Timeout(5.seconds)
  // start a new HTTP server on port 9090 with our service actor as the handler
  IO(Http) ? Http.Bind(service, interface = "localhost", port = 9090)

  Await.result(PostgresLibraryDatabase.init(), 5 second)
}
