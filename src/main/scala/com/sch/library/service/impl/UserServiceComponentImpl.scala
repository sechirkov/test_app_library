package com.sch.library.service.impl

import com.sch.library.dao.UserDaoComponent
import com.sch.library.domain.User
import com.sch.library.service.UserServiceComponent

import scala.concurrent.Future

/**
 * User: schirkov
 * Date: 9/9/2016
 */
trait UserServiceComponentImpl extends UserServiceComponent {
  this: UserDaoComponent =>
  class UserServiceImpl extends UserService {
    override def findAll(): Future[Seq[User]] = userDao.findAll()
    override def update(user: User): Future[Boolean] = userDao.update(user)
    override def persist(user: User): Future[User] = userDao.persist(user)
    override def delete(user: User): Future[Boolean] = userDao.delete(user)
  }
}
