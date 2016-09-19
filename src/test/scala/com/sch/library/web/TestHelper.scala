package com.sch.library.web

import com.sch.library.domain.User
import com.sch.library.service.ComponentRegistryForTest._

import scala.concurrent.Await
import scala.concurrent.duration.Duration

/**
 * User: schirkov
 * Date: 9/19/2016
 */
object TestHelper {

  def seconds: Int => Duration = x => Duration(x, scala.concurrent.duration.SECONDS)

  lazy val admin: User = {
    Await.result[Option[User]](userService.findByLogin("admin"), seconds(1)).get
  }

  lazy val user: User = {
    Await.result[Option[User]](userService.findByLogin("user"), seconds(1)).get
  }
}
