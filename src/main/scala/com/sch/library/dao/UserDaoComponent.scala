package com.sch.library.dao

import com.sch.library.domain.User

import scala.concurrent.Future

/**
 * User: schirkov
 * Date: 9/9/2016
 */
trait UserDaoComponent {
  val userDao: UserDao
  trait UserDao {
    def findAll(): Future[Seq[User]]
    def persist(user: User): Future[User]
    def update(user: User): Future[Boolean]
    def delete(user: User): Future[Boolean]
    def findByLogin(login: String): Future[Option[User]]
  }
}
