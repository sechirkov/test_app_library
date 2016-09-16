package com.sch.library.service

import com.sch.library.dao.LogBookDaoComponent
import com.sch.library.domain.LogBook

import scala.concurrent.Future

/**
  * User: schirkov
  * Date: 12.09.2016
  */
trait LogBookServiceComponent {
  this: LogBookDaoComponent =>
  val logbookService: LogBookService

  @scala.annotation.implicitNotFound("""Cannot find an implicit LogBookService. You might pass
    an (implicit logbookService: LogBookService) parameter to your method
    or import com.sch.library.service.ComponentRegistry.logbookService.""")
  trait LogBookService {
    def persist(logbook: LogBook): Future[LogBook]
    def returnBook(logbook: LogBook): Future[Boolean]
    def findByBookId(bookId: Long): Future[Option[LogBook]]
    def findLastEntryByBookId(bookId: Long): Future[Option[LogBook]]
  }
}
