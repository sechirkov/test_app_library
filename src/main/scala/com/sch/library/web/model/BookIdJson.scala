package com.sch.library.web.model

import spray.json.DefaultJsonProtocol

/**
  * User: schirkov
  * Date: 12.09.2016
  */
case class BookIdJson(bookId: Long)

object BookIdJson extends DefaultJsonProtocol {
  implicit val takeBookJsonFormat = jsonFormat1(BookIdJson.apply)
}
