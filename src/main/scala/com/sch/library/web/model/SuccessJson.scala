package com.sch.library.web.model

import spray.json.DefaultJsonProtocol

/**
 * User: schirkov
 * Date: 9/14/2016
 */
case class SuccessJson(success: String = "OK")

object SuccessJson extends DefaultJsonProtocol {
  implicit val successJsonFormat = jsonFormat1(SuccessJson.apply)
}
