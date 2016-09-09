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
  trait UserService {
    def findAll(): Future[Seq[User]]
    def persist(user: User): Future[User]
    def update(user: User): Future[Boolean]
    def delete(user: User): Future[Boolean]
  }
}
