package com.sch.library.web

import com.sch.library.service.ComponentRegistryForTest._
import org.specs2.mutable.Specification
import spray.http.HttpHeaders.Authorization
import spray.http.{BasicHttpCredentials, StatusCode}
import spray.testkit.Specs2RouteTest

import scala.language.postfixOps

/**
  * User: schirkov
  * Date: 15.09.2016
  */
class LibraryServiceSpec extends BeforeAllTest with Specs2RouteTest  with LibraryService with BasicAuthenticationService {
  import StatusCode.int2StatusCode

  def actorRefFactory = system

  "LibraryServiceSpec" should {

    "return home page for /index.html GET request" in {
      Get("/index.html") ~> indexRoute ~> check {
        status should_== int2StatusCode(200)
      }
    }

    "return home page for /index.html GET request (admin role)" in {
      Get("/index.html") ~> sealRoute(routes.apply(TestHelper.admin)) ~> check {
        status should_== int2StatusCode(200)
      }
    }

//    "should not return home page for /index.html GET request for unknown user" in {
//      Get("/index.html") ~> Authorization(BasicHttpCredentials("John", "")) ~> authenticate(basicAuth(authenticator = new DaoUserPassAuthenticator()))(routes) ~> check {
//        status should_== int2StatusCode(200)
//      }
//    }
  }
}
