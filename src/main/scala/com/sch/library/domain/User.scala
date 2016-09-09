package com.sch.library.domain

import com.sch.library.util.JsonEnumFormat._
import spray.json.DefaultJsonProtocol

/**
 * User: schirkov
 * Date: 9/1/2016
 */
case class User(id: Option[Long], login: String,
                firstName: String, lastName: String, secondName: Option[String],
                status: Option[UserStatus.Value] = Some(UserStatus.active)) {
  def changeStatus(newStatus: UserStatus.Value) = this.status match {
    case Some(s) if s == newStatus => this
    case _ => this.copy(status = Some(newStatus))
  }
}

object User extends DefaultJsonProtocol {
  implicit val userStatusJsonFormat = format(UserStatus)
  implicit val userJsonFormat = jsonFormat6(User.apply)
}

object UserStatus extends Enumeration {
  val active = Value(1)
  val blocked = Value(2)
}
