package com.sch.library.domain.table

import java.sql.Timestamp

import com.sch.library.domain.LogBook
import slick.driver.PostgresDriver.api._

import scala.language.postfixOps

/**
 * User: schirkov
 * Date: 9/12/2016
 */
class LogBookTable(tag: Tag) extends Table[LogBook](tag, "log_book") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def bookId = column[Long]("book_id")
  def userId = column[Long]("user_id")
  def issuedByUser = column[Long]("issued_by_user")
  def date = column[Timestamp]("date")
  def receivedByUser = column[Option[Long]]("received_by_user")
  def returnDate = column[Option[Timestamp]]("return_date")

  def fkBookId = foreignKey("fk_logbook_book", bookId, Tables.books)(_.id)
  def fkUserId = foreignKey("fk_logbook_user", userId, Tables.users)(_.id)
  def fkIssuedByUser = foreignKey("fk_logbook_issued_by", issuedByUser, Tables.users)(_.id)
  def fkReceivedByUser = foreignKey("fk_logbook_received_by", receivedByUser, Tables.users)(_.id?)

  override def * = (id.?, bookId, userId, issuedByUser, date, receivedByUser, returnDate) <> (LogBook.tupled, LogBook.unapply)
}
