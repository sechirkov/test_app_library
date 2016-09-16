package com.sch.library

import com.sch.library.domain.User
import com.sch.library.service.{LogBookServiceComponent, BookServiceComponent, UserServiceComponent}
import spray.routing.Route
import spray.routing.authentication.UserPass

import scala.concurrent.Future

/**
 * User: schirkov
 * Date: 9/15/2016
 */
package object web {
  type SecureRoute = User => Route
  type UserService = UserServiceComponent#UserService
  type BookService = BookServiceComponent#BookService
  type LogBookService = LogBookServiceComponent#LogBookService

  sealed case class CurrentUserCookieName(value: String)
  private [web] val currentUserCookieName = CurrentUserCookieName("current_user")
}
