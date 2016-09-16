package com.sch.library.service

import com.sch.library.dao.PostgresDBComponent
import com.sch.library.dao.impl.{BookDaoComponentImpl, LogBookDaoComponentImpl, UserDaoComponentImpl}
import com.sch.library.service.impl.{BookServiceComponentImpl, LogBookServiceComponentImpl, UserServiceComponentImpl}

import scala.concurrent.ExecutionContext.Implicits.global

/**
 * User: schirkov
 * Date: 9/8/2016
 */
object ComponentRegistry extends BookDaoComponentImpl with BookServiceComponentImpl
with UserDaoComponentImpl with UserServiceComponentImpl
with LogBookDaoComponentImpl with LogBookServiceComponentImpl
with PostgresDBComponent {
  implicit val bookDao = new BookDaoImpl
  implicit val bookService = new BookServiceImpl
  implicit val userDao = new UserDaoImpl
  implicit val userService = new UserServiceImpl
  implicit val logbookDao = new LogBookDaoImpl
  implicit val logbookService =new LogBookServiceImpl
}
