package com.sch.library.domain

import slick.driver.H2Driver.api._
import slick.lifted.ProvenShape

import scala.collection.mutable.ArrayBuffer
import scala.concurrent.{ExecutionContext, Future}


/**
 * User: schirkov
 * Date: 9/1/2016
 */
abstract class Library(id: Long, name: String) {
  def getBooks: Future[Seq[Book]]
}

object Library {
  lazy val listOfBook = ArrayBuffer(
    Book(Some(1), "Programming in Scala Second Edition", "Martin Odersky, Lex Spoon, Bill Venners", Some("asdf431g")),
    Book(Some(2), "Scala By Example", "Martin Odersky", Some("qwert54y")),
    Book(Some(3), "Scala Cookbook: Recipes for Object-Oriented and Functional Programming", "Alvin Alexander", Some("zxcv765b"))
  )

  val library = new Library(1, "Office library") {
    override def getBooks: Future[Seq[Book]] = {
      db.run(books.result)
    }
  }

  lazy val db = Database.forConfig("h2mem1")
  lazy val books = TableQuery[BookTable]

  def initDatabase()(implicit executionContext: ExecutionContext): Unit = {
    val setup = DBIO.seq(
      books.schema.create,
      books ++= listOfBook
    )
    db.run(setup.transactionally)
  }
}

final class BookTable(tag: Tag) extends Table[Book](tag, "book") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def name = column[String]("name")
  def authors = column[String]("authors")
  def inventoryNumber = column[String]("inventory_number")
  def status = column[BookStatus.Value]("status")
  override def * : ProvenShape[Book] = (id.?, name, authors, inventoryNumber.?, status) <> ((Book.apply _).tupled, Book.unapply)

  implicit val enumMapper = MappedColumnType.base[BookStatus.Value, String](_.toString, BookStatus.withName)
}

//id: Option[Long], name: String, authors: String,
//                inventoryNumber: Option[String],
//                status: Option[BookStatus.Value] = Some(BookStatus.available)
