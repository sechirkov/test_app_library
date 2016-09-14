package com.sch.library.dao

/**
 * User: schirkov
 * Date: 9/14/2016
 */
trait PostgresDBComponent extends DBComponent {
  val driver = slick.driver.PostgresDriver

  import slick.driver.PostgresDriver.api._

  val db = Database.forConfig("postgres")
}
