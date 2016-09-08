package com.sch.library.dao

import slick.driver.PostgresDriver.api._
import slick.jdbc.JdbcBackend

/**
 * User: schirkov
 * Date: 9/8/2016
 */
trait DB {
  //lazy val db = Database.forConfig("h2mem1")
    lazy val db:JdbcBackend#Database = Database.forConfig("postgres")
}
