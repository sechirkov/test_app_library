package com.sch.library.dao.table

import com.sch.library.dao.DBComponent
import com.sch.library.dao.table.Implicits.Mappings._
import com.sch.library.domain.{Book, BookStatus}

/**
 * User: schirkov
 * Date: 9/8/2016
 */
private[dao] trait BookTable {
  this: DBComponent =>
  import driver.api._

  private[table] class BookTable(tag: Tag) extends Table[Book](tag, "book") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def title = column[String]("title")
    def authors = column[String]("authors")
    def inventoryNumber = column[String]("inventory_number")
    def status = column[BookStatus.Value]("status", O.Default(BookStatus.available))

    def uniqueInvNumber = index("idx_book_inv_num", inventoryNumber, unique = true)

    override def * = (id.?, title, authors, inventoryNumber.?, status) <> ((Book.apply  _).tupled, Book.unapply)
  }

  protected val books = TableQuery[BookTable]
}

