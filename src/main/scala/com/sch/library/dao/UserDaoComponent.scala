package com.sch.library.dao

import com.sch.library.domain.User

import scala.concurrent.Future

/**
 * User: schirkov
 * Date: 9/9/2016
 */
trait UserDaoComponent {
  val userDao: UserDao
  trait UserDao extends GenericDao[User] {
    def findByLogin(login: String): Future[Option[User]]
  }
}
