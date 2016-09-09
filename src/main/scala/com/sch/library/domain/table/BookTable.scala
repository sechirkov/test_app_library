package com.sch.library.domain.table

import com.sch.library.domain.table.Implicits.Mappings._
import com.sch.library.domain.{Book, BookStatus}
import slick.driver.PostgresDriver.api._

/**
 * User: schirkov
 * Date: 9/8/2016
 */
final class BookTable(tag: Tag) extends Table[Book](tag, "book") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def title = column[String]("title")
  def authors = column[String]("authors")
  def inventoryNumber = column[String]("inventory_number")
  def status = column[BookStatus.Value]("status", O.Default(BookStatus.available))

  def uniqueInvNumber = index("idx_book_inv_num", inventoryNumber, unique = true)

  override def * = (id.?, title, authors, inventoryNumber.?, status) <> ((Book.apply  _).tupled, Book.unapply)
}
