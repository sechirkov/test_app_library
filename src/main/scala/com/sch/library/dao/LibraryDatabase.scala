package com.sch.library.dao

import com.sch.library.dao.table.{LogBookTable, UserTable, BookTable}
import com.sch.library.domain.{Book, User}

import slick.jdbc.meta.MTable

import scala.concurrent.{ExecutionContext, Future}

/**
 * User: schirkov
 * Date: 9/8/2016
 */
abstract class LibraryDatabase extends BookTable with LogBookTable with UserTable { this: DBComponent =>
  import driver.api._
  import slick.dbio.DBIO

  lazy val listOfBook = List(
    Book(None, "Programming in Scala Second Edition", "Martin Odersky, Lex Spoon, Bill Venners", Some("asdf431g")),
    Book(None, "Scala By Example", "Martin Odersky", Some("qwert54y")),
    Book(None, "Scala Cookbook: Recipes for Object-Oriented and Functional Programming", "Alvin Alexander", Some("zxcv765b"))
  )

  lazy val userList = List(
    User(None, "user", "Ivonov", "Ivan", Some("Ivanovich")),
    User(None, "admin", "Adminov", "Admin", None)
  )

  def init()(implicit executionContext: ExecutionContext) = {
    checkTableExistenceAndDoAction("book", db.run(DBIO.seq(
      books.schema.create,
      books ++= listOfBook
    ))) flatMap { case _ =>
      checkTableExistenceAndDoAction("user", db.run(DBIO.seq(
        users.schema.create,
        users ++= userList
      ))) flatMap { case _ =>
        checkTableExistenceAndDoAction("log_book", db.run(DBIO.seq(
          logbooks.schema.create
        )))
      } andThen {
        case _ => db.close()
      }
    }
  }

  private def checkTableExistenceAndDoAction(tableName: String, action: => Unit)(implicit executionContext: ExecutionContext): Future[_] = {
    db.run(MTable.getTables(tableName)).flatMap(v => Future {
      if (v.isEmpty) action
    })
  }
}

object PostgresLibraryDatabase extends LibraryDatabase with PostgresDBComponent

object H2LibraryDatabase extends LibraryDatabase with H2DBComponent
