package com.sch.library.dao

import com.sch.library.domain.table.Tables._
import com.sch.library.domain.{Book, User}
import slick.dbio.DBIO
import slick.driver.PostgresDriver.api._
import slick.jdbc.meta.MTable

import scala.concurrent.ExecutionContext
import scala.util.Success

/**
 * User: schirkov
 * Date: 9/8/2016
 */
object LibraryDatabase extends DB {
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
    for {
      _ <- db.run(MTable.getTables("book")).andThen({
        case Success(v) => if (v.isEmpty) db.run(DBIO.seq(
          books.schema.create,
          books ++= listOfBook
        ))
        case _ => db.close()
      })

      _ <- db.run(MTable.getTables("user")).andThen({
        case Success(v) => if (v.isEmpty) db.run(DBIO.seq(
          users.schema.create,
          users ++= userList
        ))
        case _ => db.close()
      })
    } yield ()
  }
}
