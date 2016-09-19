package com.sch.library.web

import org.specs2.mutable.Specification
import spray.http.{HttpResponse, StatusCode}
import spray.testkit.Specs2RouteTest

class StaticContentServiceSpec extends Specification with Specs2RouteTest with StaticContentService {
  import StatusCode.int2StatusCode

  def actorRefFactory = system

  "StaticContentService" should {

    "return a JavaScript for GET requests" in {
      Get("/js/jquery.cookie.js") ~> staticResources ~> check {
        status should_== int2StatusCode(200)
      }
    }

    "return a CSS file for GET requests" in {
      Get("/js/jquery-ui-1.12.0.custom/jquery-ui.css") ~> staticResources ~> check {
        status should_== int2StatusCode(200)
      }
    }

    "return a PNG file for GET requests" in {
      Get("/js/jquery-ui-1.12.0.custom/images/ui-icons_444444_256x240.png") ~> staticResources ~> check {
        status should_== int2StatusCode(200)
      }
    }

    "leave GET requests to other paths unhandled" in {
      Get("/views/add-book.html") ~> staticResources ~> check {
        handled must beFalse
      }
    }
  }
}
