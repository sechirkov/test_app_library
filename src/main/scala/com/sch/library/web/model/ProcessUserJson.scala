package com.sch.library.web.model

import com.sch.library.util.JsonEnumFormat._
import spray.json.DefaultJsonProtocol

/**
 * User: schirkov
 * Date: 9/14/2016
 */
case class UserSessionJson(action: UserAction.Value, login: Option[String])

object UserSessionJson extends DefaultJsonProtocol {
  implicit val actionEnumJsonFormat = format(UserAction)
  implicit val processUserJsonFormat = jsonFormat2(UserSessionJson.apply)
}

object UserAction extends Enumeration {
  val startWork = Value("start-work")
  val endWork = Value("end-work")
}
