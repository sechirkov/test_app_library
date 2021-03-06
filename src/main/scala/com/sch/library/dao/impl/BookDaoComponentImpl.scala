package com.sch.library.dao.impl

import com.sch.library.dao.{DBComponent, BookDaoComponent}
import com.sch.library.domain.Book
import com.sch.library.dao.table.{LogBookTable, BookTable}

import scala.concurrent.{ExecutionContext, Future}

/**
 * User: schirkov
 * Date: 9/8/2016
 */
trait BookDaoComponentImpl extends BookDaoComponent with BookTable with LogBookTable {
  this: DBComponent =>
  import driver.api._

  class BookDaoImpl(implicit val executionContext: ExecutionContext) extends BookDao {
    override type ID = Long
    override def findAll(): Future[Seq[Book]] = db.run(books.result)
    override def persist(book: Book): Future[Book] = {
      val insertQuery = books returning books.map(_.id) into ((book, id) => book.copy(id=Some(id)))
      db.run(insertQuery += book)
    }
    override def delete(book: Book): Future[Boolean] = db.run(books.filter(_.id === book.id).delete.map(_ > 0))
    override def findAvailable(): Future[Seq[Book]] = {
      val query = (for {
        b <- books if !logbooks.filter(_.bookId === b.id).filter(_.returnDate.isEmpty).map(_ => 1).exists
      } yield b).result
      //query.statements.foreach(println)
      db.run(query)
    }
    override def find(id: ID): Future[Option[Book]] = throw new UnsupportedOperationException
    override def findBooksTakenByUser(userId: Long): Future[Seq[Book]] = {
      val query = (for {
        b <- books if logbooks.filter(_.bookId === b.id).filter(_.returnDate.isEmpty).filter(_.userId === userId).map(_ => 1).exists
      } yield b).result
      query.statements.foreach(println)
      db.run(query)
    }
  }
}
