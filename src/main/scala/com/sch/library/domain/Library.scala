package com.sch.library.domain

import spray.json.DefaultJsonProtocol

import scala.collection.mutable.ArrayBuffer

/**
 * User: schirkov
 * Date: 9/1/2016
 */
abstract class Library(id: Long, name: String) {
  val books: ArrayBuffer[Book]
}

object Library {
  lazy val listOfBook = ArrayBuffer(
    Book(Some(1), "Programming in Scala Second Edition", "Martin Odersky, Lex Spoon, Bill Venners", Some("asdf431g")),
    Book(Some(2), "Scala By Example", "Martin Odersky", Some("qwert54y")),
    Book(Some(3), "Scala Cookbook: Recipes for Object-Oriented and Functional Programming", "Alvin Alexander", Some("zxcv765b"))
  )

  lazy val library = new Library(1, "Office library") {
    override val books: ArrayBuffer[Book] = listOfBook
  }
}

