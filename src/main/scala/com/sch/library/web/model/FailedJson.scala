package com.sch.library.web.model

import spray.json.DefaultJsonProtocol

/**
  * User: schirkov
  * Date: 12.09.2016
  */
case class FailedJson(error: String)

object FailedJson extends DefaultJsonProtocol {
  implicit val failedJsonFormat = jsonFormat1(FailedJson.apply)
}
