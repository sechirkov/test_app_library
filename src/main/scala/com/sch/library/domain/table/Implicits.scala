package com.sch.library.domain.table

import com.sch.library.domain.BookStatus
import slick.driver.PostgresDriver.api._

/**
 * User: schirkov
 * Date: 9/8/2016
 */
object Implicits {
  object Mappings {
    implicit val bookStatusMapper = MappedColumnType.base[BookStatus.Value, String](_.toString, BookStatus.withName)
  }
}
