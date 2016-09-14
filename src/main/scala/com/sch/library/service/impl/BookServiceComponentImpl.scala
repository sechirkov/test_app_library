package com.sch.library.service.impl

import java.util.Date

import com.sch.library.dao.BookDaoComponent
import com.sch.library.domain.Book
import com.sch.library.service.BookServiceComponent
import com.sch.library.util.InventoryNumberGenerator

import scala.concurrent.Future

/**
 * User: schirkov
 * Date: 9/8/2016
 */
trait BookServiceComponentImpl extends BookServiceComponent {
  this: BookDaoComponent =>
  class BookServiceImpl extends BookService {
    override def findAll(): Future[Seq[Book]] = bookDao.findAll()
    override def persist(book: Book): Future[Book] = bookDao.persist(book)
    override def delete(book: Book): Future[Boolean] = bookDao.delete(book)
    override def findAvailable(): Future[Seq[Book]] = bookDao.findAvailable()
    override def findBooksTakenByUser(userId: Long): Future[Seq[Book]] = bookDao.findBooksTakenByUser(userId)
  }
}
