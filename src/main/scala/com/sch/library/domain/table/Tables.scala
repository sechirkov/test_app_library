package com.sch.library.domain.table

import slick.lifted.TableQuery

/**
 * User: schirkov
 * Date: 9/8/2016
 */
object Tables {
  lazy val books = TableQuery[BookTable]
  lazy val users = TableQuery[UserTable]
}
