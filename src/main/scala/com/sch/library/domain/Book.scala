package com.sch.library.domain

import com.sch.library.util.JsonEnumFormat._
import spray.json.DefaultJsonProtocol

/**
  * User: schirkov
  * Date: 9/5/2016
  */



//case class Book(id: Long, name: String, authors: List[Author], inventoryNumber: String, status: BookStatus.Value) problem with json format List[Author]
case class Book(id: Option[Long] = None, title: String, authors: String,
                inventoryNumber: Option[String] = None,
                status: BookStatus.Value = BookStatus.available) {
}

object Book extends DefaultJsonProtocol {
  implicit val bookStatusJsonFormat = format(BookStatus)
  implicit val bookJsonFormat5 = jsonFormat5(Book.apply)
}

object BookStatus extends Enumeration with DefaultJsonProtocol {
  val available = Value(1)
  val other = Value(2)
}
