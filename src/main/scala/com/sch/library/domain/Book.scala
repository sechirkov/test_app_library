package com.sch.library.domain

import com.sch.library.util.JsonEnumFormat._
import spray.json.DefaultJsonProtocol

/**
  * User: schirkov
  * Date: 9/5/2016
  */



//case class Book(id: Long, name: String, authors: List[Author], inventoryNumber: String, status: BookStatus.Value) problem with json format List[Author]
case class Book(id: Option[Long], name: String, authors: String,
                inventoryNumber: Option[String],
                status: Option[BookStatus.Value] = Some(BookStatus.available))

object Book extends DefaultJsonProtocol {
  implicit val bookStatusJsonFormat = format(BookStatus)
  implicit val bookJsonFormat = jsonFormat5(Book.apply)
}

object BookStatus extends Enumeration with DefaultJsonProtocol {
  val available = Value(1)
  val other = Value(2)


}
