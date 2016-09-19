package com.sch.library.web

import com.sch.library.dao.H2LibraryDatabase
import org.specs2.mutable.Specification
import org.specs2.specification.{Step, Fragments}

import scala.concurrent.Await

/**
 * User: schirkov
 * Date: 9/19/2016
 */
trait BeforeAllTest extends Specification {
  override def map(fragments: => Fragments) = Step(beforeAll()) ^ fragments

  protected def beforeAll() = Await.result(H2LibraryDatabase.init(), TestHelper.seconds(2))
}
