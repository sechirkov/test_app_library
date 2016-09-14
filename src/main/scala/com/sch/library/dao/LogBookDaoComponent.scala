package com.sch.library.dao

import com.sch.library.domain.LogBook

import scala.concurrent.Future

/**
  * User: schirkov
  * Date: 12.09.2016
  */
trait LogBookDaoComponent {
  val logbookDao:LogBookDao
  trait LogBookDao extends GenericDao[LogBook] {
    def findByBookId(bookId: Long): Future[Option[LogBook]]
    def findLastEntryByBookId(bookId: Long): Future[Option[LogBook]]
    def update(logbook: LogBook): Future[Boolean]
  }
}
