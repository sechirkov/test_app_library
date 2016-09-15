package com.sch.library.dao.impl

import com.sch.library.dao.table.LogBookTable
import com.sch.library.dao.{DBComponent, LogBookDaoComponent}
import com.sch.library.domain.LogBook

import scala.concurrent.{ExecutionContext, Future}

/**
  * User: schirkov
  * Date: 12.09.2016
  */
trait LogBookDaoComponentImpl extends LogBookDaoComponent with LogBookTable {
  this: DBComponent =>
  import driver.api._

  class LogBookDaoImpl(implicit val executionContext: ExecutionContext) extends LogBookDao {
    override type ID = Long
    override def persist(logbook: LogBook): Future[LogBook] = {
      val insertQuery = logbooks returning logbooks.map(_.id) into ((logbook, id) => logbook.copy(id=Some(id)))
      db.run(insertQuery += logbook)
    }
    override def returnBook(logbook: LogBook): Future[Boolean] = {
      val query = logbooks.filter(_.id === logbook.id).map(lb => (lb.receivedByUser, lb.returnDate)).update((logbook.receivedByUser, logbook.returnDate))
      db.run(query.map(_ > 0))
    }
    override def findLastEntryByBookId(bookId: Long): Future[Option[LogBook]] = db.run(logbooks.filter(_.bookId === bookId).filter(_.returnDate.isEmpty).result.headOption)
    override def findByBookId(bookId: Long): Future[Option[LogBook]] = db.run(logbooks.filter(_.bookId === bookId).result.headOption)
    override def findAll(): Future[Seq[LogBook]] = throw new UnsupportedOperationException
    override def delete(entity: LogBook): Future[Boolean] = throw new UnsupportedOperationException
    override def find(id: ID): Future[Option[LogBook]] = throw new UnsupportedOperationException
  }
}
