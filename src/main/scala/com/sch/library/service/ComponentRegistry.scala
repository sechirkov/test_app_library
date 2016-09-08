package com.sch.library.service

import com.sch.library.dao.impl.BookDaoComponentImpl
import com.sch.library.service.impl.BookServiceComponentImpl

import scala.concurrent.ExecutionContext
import ExecutionContext.Implicits.global

/**
 * User: schirkov
 * Date: 9/8/2016
 */
object ComponentRegistry extends BookDaoComponentImpl with BookServiceComponentImpl {
  val bookDao = new BookDaoImpl()
  val bookService = new BookServiceImpl()
}
