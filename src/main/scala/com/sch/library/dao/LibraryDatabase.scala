package com.sch.library.dao

import com.sch.library.domain.table.Tables._
import com.sch.library.domain.{Book, User}
import slick.dbio.DBIO
import slick.driver.PostgresDriver.api._
import slick.jdbc.meta.MTable

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer
import scala.concurrent.{ExecutionContext, Future}

/**
 * User: schirkov
 * Date: 9/8/2016
 */
object LibraryDatabase extends DB {
  type TableName = String
  type DBAction = slick.dbio.DBIOAction[_, slick.dbio.NoStream, _ <:Effect]

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
    val actions = new ArrayBuffer[(TableName, DBAction)]
    actions += (("book", DBIO.seq(
      books.schema.create,
      books ++= listOfBook
    )))
    actions += (("user", DBIO.seq(
      users.schema.create,
      users ++= userList
    )))

    actions.foldLeft[Future[_]](Future { })((f, a) => f andThen { case _ => checkTableExistenceAndDoAction(a._1, db.run(a._2)) }) andThen { case _ => db.close() }

    /*checkTableExistenceAndDoAction("book", db.run(DBIO.seq(
      books.schema.create,
      books ++= listOfBook
    ))) andThen { case _ =>
      checkTableExistenceAndDoAction("user", db.run(DBIO.seq(
        users.schema.create,
        users ++= userList
      ))) andThen {
        case _ => db.close()
      }
    }*/
  }

  private def checkTableExistenceAndDoAction(tableName: String, action: => Unit)(implicit executionContext: ExecutionContext): Future[_] = {
    db.run(MTable.getTables(tableName)).flatMap(v => Future { if (v.isEmpty) action })
  }
}
