package com.sch.library.service

import com.sch.library.dao.UserDaoComponent
import com.sch.library.domain.User

import scala.concurrent.Future

/**
 * User: schirkov
 * Date: 9/9/2016
 */
trait UserServiceComponent {
  this: UserDaoComponent =>
  val userService: UserService

  @scala.annotation.implicitNotFound("""Cannot find an implicit UserService. You might pass
    an (implicit userService: UserService) parameter to your method
    or import com.sch.library.service.ComponentRegistry.userService.""")
  trait UserService {
    def findAll(): Future[Seq[User]]
    def persist(user: User): Future[User]
    def delete(user: User): Future[Boolean]
    def findByLogin(login: String): Future[Option[User]]
  }
}
