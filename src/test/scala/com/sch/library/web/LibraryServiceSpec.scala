package com.sch.library.web

import com.sch.library.service.{BookServiceComponent, ComponentRegistryForTest, LogBookServiceComponent, UserServiceComponent}
import org.specs2.mutable.{Specification, SpecificationLike}
import spray.http.StatusCode
import spray.testkit.Specs2RouteTest

/**
  * User: schirkov
  * Date: 15.09.2016
  */
class LibraryServiceSpec extends  {
  override val userService: UserServiceComponent#UserService = ComponentRegistryForTest.userService
  override val bookService: BookServiceComponent#BookService = ComponentRegistryForTest.bookService
  override val logbookService: LogBookServiceComponent#LogBookService = ComponentRegistryForTest.logbookService
} with SpecificationLike with Specs2RouteTest with LibraryService {
  import StatusCode.int2StatusCode

  def actorRefFactory = system

  "LibraryServiceSpec" should {

    "return home page for /index.html GET request" in {
      Get("index.html") ~> indexRoute ~> check {
        status should_== int2StatusCode(200)
      }
    }
  }
}
