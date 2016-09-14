package com.sch.library.service

import com.sch.library.dao.BookDaoComponent
import com.sch.library.domain.Book

import scala.concurrent.Future

/**
 * User: schirkov
 * Date: 9/8/2016
 */
trait BookServiceComponent {
  this: BookDaoComponent =>
  val bookService:BookService

  trait BookService {
    def findAll(): Future[Seq[Book]]
    def persist(book: Book): Future[Book]
    def delete(book: Book): Future[Boolean]
    def findAvailable(): Future[Seq[Book]]
    def findBooksTakenByUser(userId: Long): Future[Seq[Book]]
  }
}
