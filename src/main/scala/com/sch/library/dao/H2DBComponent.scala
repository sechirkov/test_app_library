package com.sch.library.dao

/**
 * User: schirkov
 * Date: 9/14/2016
 */
trait H2DBComponent extends DBComponent {
  val driver = slick.driver.H2Driver

  import slick.driver.H2Driver.api._

  val db = Database.forConfig("h2mem1")
}
