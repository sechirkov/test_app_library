package com.sch.library.web.model

import com.sch.library.domain.User
import spray.json.DefaultJsonProtocol

/**
 * User: schirkov
 * Date: 9/13/2016
 */
case class UserListJson(success: String = "OK", data: Seq[User])

object UserListJson extends DefaultJsonProtocol {
  implicit val userListJsonFormat = jsonFormat2(UserListJson.apply)
}


