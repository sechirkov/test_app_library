package com.sch.library.dao

import com.sch.library.domain.Book

import scala.concurrent.Future

/**
 * User: schirkov
 * Date: 9/8/2016
 */
trait BookDaoComponent {
  val bookDao: BookDao
  trait BookDao extends GenericDao[Book] {
    def findAvailable(): Future[Seq[Book]]
    def findBooksTakenByUser(userId: Long): Future[Seq[Book]]
  }
}