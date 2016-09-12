package com.sch.library.service.impl

import com.sch.library.dao.LogBookDaoComponent
import com.sch.library.domain.LogBook
import com.sch.library.service.LogBookServiceComponent

import scala.concurrent.Future

/**
  * User: schirkov
  * Date: 12.09.2016
  */
trait LogBookServiceComponentImpl extends LogBookServiceComponent {
  this: LogBookDaoComponent =>
  class LogBookServiceImpl extends LogBookService {
    override def persist(logbook: LogBook): Future[LogBook] = logbookDao.persist(logbook)
    override def update(logbook: LogBook): Future[Boolean] = logbookDao.update(logbook)
    override def findLastEntryByBookId(bookId: Long): Future[Option[LogBook]] = logbookDao.findLastEntryByBookId(bookId)
    override def findByBookId(bookId: Long): Future[Option[LogBook]] = logbookDao.findByBookId(bookId)
  }
}
