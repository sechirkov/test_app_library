package com.sch.library.service

import com.sch.library.dao.H2DBComponent
import com.sch.library.dao.impl.{BookDaoComponentImpl, LogBookDaoComponentImpl, UserDaoComponentImpl}
import com.sch.library.service.impl.{BookServiceComponentImpl, LogBookServiceComponentImpl, UserServiceComponentImpl}

import scala.concurrent.ExecutionContext.Implicits.global

/**
  * User: schirkov
  * Date: 15.09.2016
  */
object ComponentRegistryForTest extends BookDaoComponentImpl with BookServiceComponentImpl
  with UserDaoComponentImpl with UserServiceComponentImpl
  with LogBookDaoComponentImpl with LogBookServiceComponentImpl with H2DBComponent {
  val bookDao = new BookDaoImpl
  val bookService = new BookServiceImpl
  val userDao = new UserDaoImpl
  val userService = new UserServiceImpl
  val logbookDao = new LogBookDaoImpl
  val logbookService =new LogBookServiceImpl
}
