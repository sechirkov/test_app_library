package com.sch.library.service

import com.sch.library.dao.DB
import com.sch.library.dao.impl.{BookDaoComponentImpl, LogBookDaoComponentImpl, UserDaoComponentImpl}
import com.sch.library.service.impl.{BookServiceComponentImpl, LogBookServiceComponentImpl, UserServiceComponentImpl}

import scala.concurrent.ExecutionContext
import ExecutionContext.Implicits.global

/**
 * User: schirkov
 * Date: 9/8/2016
 */
object ComponentRegistry extends BookDaoComponentImpl with BookServiceComponentImpl
with UserDaoComponentImpl with UserServiceComponentImpl
with LogBookDaoComponentImpl with LogBookServiceComponentImpl
with DB {
  val bookDao = new BookDaoImpl
  val bookService = new BookServiceImpl
  val userDao = new UserDaoImpl
  val userService = new UserServiceImpl
  val logbookDao = new LogBookDaoImpl
  val logbookService =new LogBookServiceImpl
}
