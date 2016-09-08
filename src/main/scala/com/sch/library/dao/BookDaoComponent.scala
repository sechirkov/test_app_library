package com.sch.library.dao

import com.sch.library.domain.Book

import scala.concurrent.Future

/**
 * User: schirkov
 * Date: 9/8/2016
 */
trait BookDaoComponent {
  val bookDao: BookDao
  trait BookDao {
    def findAll(): Future[Seq[Book]]
    def persist(book: Book): Future[Book]
    def update(book: Book): Future[Boolean]
    def delete(book: Book): Future[Boolean]
  }
}