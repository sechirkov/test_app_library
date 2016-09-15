package com.sch.library.web.model

import com.sch.library.util.JsonEnumFormat._
import spray.json.DefaultJsonProtocol

/**
 * User: schirkov
 * Date: 9/14/2016
 */
case class ProcessUserJson(action: UserAction.Value, login: Option[String])

object ProcessUserJson extends DefaultJsonProtocol {
  implicit val processUserJsonFormat = jsonFormat2(ProcessUserJson.apply)
  implicit val actionEnumJsonFormat = format(UserAction)
}

object UserAction extends Enumeration {
  val startWork = Value("start-work")
  val endWork = Value("end-work")
}
