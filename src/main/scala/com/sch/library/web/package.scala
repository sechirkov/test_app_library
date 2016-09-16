package com.sch.library

import com.sch.library.domain.User
import spray.routing.Route
import spray.routing.authentication.UserPass

import scala.concurrent.Future

/**
 * User: schirkov
 * Date: 9/15/2016
 */
package object web {
  type SecureRoute = User => Route

  val currentUserCookieName = "current_user"
}
