package com.sch.library.dao.table

import com.sch.library.dao.DBComponent
import com.sch.library.dao.table.Implicits.Mappings._
import com.sch.library.domain.{User, UserStatus}

/**
 * User: schirkov
 * Date: 9/9/2016
 */
private[dao] trait UserTable {
  this: DBComponent =>
  import driver.api._

  private[table] class UserTable(tag: Tag) extends Table[User](tag, "user") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def login = column[String]("login")
    def firstName = column[String]("first_name")
    def lastName = column[String]("last_name")
    def secondName = column[Option[String]]("second_name")
    def status = column[UserStatus.Value]("status", O.Default(UserStatus.active))
    def uniqueLoginIdx = index("idx_user_login", login, unique = true)

    override def * = (id.?, login, firstName, lastName, secondName, status.?) <>((User.apply _).tupled, User.unapply)
  }

  protected val users = TableQuery[UserTable]
}
