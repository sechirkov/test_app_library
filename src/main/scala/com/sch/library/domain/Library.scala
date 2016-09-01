package com.sch.library.domain

import spray.json.DefaultJsonProtocol

/**
 * User: schirkov
 * Date: 9/1/2016
 */
abstract class Library(id: Long, name: String) {
  val books: List[ConcreteBook]
}

object Library {
  lazy val listOfBook = List(
    Book("Programming in Scala Second Edition", "Martin Odersky, Lex Spoon, Bill Venners"),
    Book("Scala By Example", "Martin Odersky"),
    Book("Scala Cookbook: Recipes for Object-Oriented and Functional Programming", "Alvin Alexander")
//    Book("Programming in Scala Second Edition",
//      List(
//        Author("Martin", "Odersky", null),
//        Author("Lex", "Spoon", null),
//        Author("Bill", "Venners", null))),
//    Book("Scala By Example", List(Author("Martin", "Odersky", null))),
//    Book("Scala Cookbook: Recipes for Object-Oriented and Functional Programming", List(Author("Alvin", "Alexander", null)))
  )

  lazy val booksInLibrary = List(
    ConcreteBook("asdf431g", listOfBook(0)),
    ConcreteBook("qwert54y", listOfBook(1)),
    ConcreteBook("zxcv765b", listOfBook(2))
  )

  lazy val library = new Library(1, "Office library") {
    override val books: List[ConcreteBook] = booksInLibrary
  }
}

//case class Book(name: String, authors: List[Author]) problem with json format List[Author]
case class Book(name: String, authors: String)

object Book extends DefaultJsonProtocol {
  implicit val bookJsonFormat = jsonFormat2(Book.apply)
}

case class ConcreteBook(inventoryNumber: String, book: Book)

object ConcreteBook extends DefaultJsonProtocol {
  implicit val concreteBookJsonFormat = jsonFormat2(ConcreteBook.apply)
}

case class Author(firstName: String, lastName: String, secondName: String) {
  override def toString = this.productIterator.withFilter(_ != null) mkString " "
}

object Author extends DefaultJsonProtocol {
  implicit val authorJsonFormat = jsonFormat3(Author.apply)
}



object BookStatus extends Enumeration {
  val available = Value(1)
  val is_taken = Value(2)
  val other = Value(3)
}


