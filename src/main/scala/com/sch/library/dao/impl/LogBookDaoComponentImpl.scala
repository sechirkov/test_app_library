package com.sch.library.dao.impl

import com.sch.library.dao.{DB, LogBookDaoComponent}
import com.sch.library.domain.LogBook
import com.sch.library.domain.table.Tables
import slick.driver.PostgresDriver.api._

import scala.concurrent.{ExecutionContext, Future}

/**
  * User: schirkov
  * Date: 12.09.2016
  */
trait LogBookDaoComponentImpl extends LogBookDaoComponent {
  this: DB =>
  class LogBookDaoImpl(implicit val executionContext: ExecutionContext) extends LogBookDao {
    override type ID = Long
    import Tables._
    override def persist(logbook: LogBook): Future[LogBook] = {
      val insertQuery = logbooks returning logbooks.map(_.id) into ((logbook, id) => logbook.copy(id=Some(id)))
      db.run(insertQuery += logbook)
    }
    override def update(logbook: LogBook): Future[Boolean] = {
      val query = logbooks.filter(_.id === logbook.id).map(lb => (lb.receivedByUser, lb.returnDate)).update((logbook.receivedByUser, logbook.returnDate))
      query.statements.foreach(println)
      db.run(query.map(_ > 0)) //todo refactor update query
    }
    override def findLastEntryByBookId(bookId: Long): Future[Option[LogBook]] = db.run(logbooks.filter(_.bookId === bookId).filter(_.returnDate.isEmpty).result.headOption)
    override def findByBookId(bookId: Long): Future[Option[LogBook]] = db.run(logbooks.filter(_.bookId === bookId).result.headOption)
    override def findAll(): Future[Seq[LogBook]] = throw new UnsupportedOperationException
    override def delete(entity: LogBook): Future[Boolean] = throw new UnsupportedOperationException
    override def find(id: ID): Future[Option[LogBook]] = throw new UnsupportedOperationException
  }
}
