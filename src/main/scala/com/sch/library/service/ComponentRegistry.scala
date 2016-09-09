package com.sch.library.service

import com.sch.library.dao.DB
import com.sch.library.dao.impl.{UserDaoComponentImpl, BookDaoComponentImpl}
import com.sch.library.service.impl.{UserServiceComponentImpl, BookServiceComponentImpl}

import scala.concurrent.ExecutionContext
import ExecutionContext.Implicits.global

/**
 * User: schirkov
 * Date: 9/8/2016
 */
object ComponentRegistry extends BookDaoComponentImpl with BookServiceComponentImpl
with UserDaoComponentImpl with UserServiceComponentImpl
with DB {
  val bookDao = new BookDaoImpl()
  val bookService = new BookServiceImpl()
  val userDao = new UserDaoImpl()
  val userService = new UserServiceImpl()
}
