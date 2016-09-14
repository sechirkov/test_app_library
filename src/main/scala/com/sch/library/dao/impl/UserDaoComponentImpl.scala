package com.sch.library.dao.impl

import com.sch.library.dao.table.UserTable
import com.sch.library.dao.{DBComponent, UserDaoComponent}
import com.sch.library.domain.User

import scala.concurrent.{ExecutionContext, Future}

/**
 * User: schirkov
 * Date: 9/9/2016
 */
trait UserDaoComponentImpl extends UserDaoComponent with UserTable {
  this: DBComponent =>
  import driver.api._

  class UserDaoImpl(implicit val executionContext: ExecutionContext) extends UserDao {
    type ID = Long
    override def findAll(): Future[Seq[User]] = db.run(users.result)
    override def persist(user: User): Future[User] = {
      val insertQuery = users returning users.map(_.id) into ((user, id) => user.copy(id = Some(id)))
      db.run(insertQuery += user)
    }
    override def delete(user: User): Future[Boolean] = db.run(users.filter(_.id === user.id).delete.map(_ > 0))
    override def find(id: ID): Future[Option[User]] = db.run(users.filter(_.id === id).result.headOption)
    override def findByLogin(login: String): Future[Option[User]] = db.run(users.filter(_.login === login).result.headOption)
  }
}
