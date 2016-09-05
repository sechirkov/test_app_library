package com.sch.library.domain

import spray.json.DefaultJsonProtocol

/**
  * User: schirkov
  * Date: 05.09.2016
  */
case class Author(firstName: String, lastName: String, secondName: String) {
  override def toString = this.productIterator.withFilter(_ != null) mkString " "
}

object Author extends DefaultJsonProtocol {
  implicit val authorJsonFormat = jsonFormat3(Author.apply)
}
