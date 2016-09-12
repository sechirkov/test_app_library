package com.sch.library.web.model

import com.sch.library.domain.Book
import spray.json.DefaultJsonProtocol

/**
  * User: schirkov
  * Date: 12.09.2016
  */
case class BookListJson(success: String = "OK", data: Seq[Book])

object BookListJson extends DefaultJsonProtocol {
  implicit val bookListJsonFormat = jsonFormat2(BookListJson.apply)
}