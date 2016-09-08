package com.sch.library.dao

import com.sch.library.domain.Book
import com.sch.library.domain.table.Tables._
import slick.dbio.DBIO
import slick.driver.PostgresDriver.api._

import scala.collection.mutable.ArrayBuffer
import scala.concurrent.ExecutionContext

/**
 * User: schirkov
 * Date: 9/8/2016
 */
object LibraryDatabase extends DB {
  lazy val listOfBook = ArrayBuffer(
    Book(None, "Programming in Scala Second Edition", "Martin Odersky, Lex Spoon, Bill Venners", Some("asdf431g")),
    Book(None, "Scala By Example", "Martin Odersky", Some("qwert54y")),
    Book(None, "Scala Cookbook: Recipes for Object-Oriented and Functional Programming", "Alvin Alexander", Some("zxcv765b"))
  )

  def init()(implicit executionContext: ExecutionContext) = {
    val setup = DBIO.seq(
      books.schema.create,
      books ++= listOfBook
    )
    db.run(setup)
  }
}
