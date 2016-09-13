package com.sch.library.domain.table

import com.sch.library.domain.table.Implicits.Mappings._
import com.sch.library.domain.{User, UserStatus}
import slick.driver.PostgresDriver.api._

/**
 * User: schirkov
 * Date: 9/9/2016
 */
final class UserTable(tag: Tag) extends Table[User](tag, "user"){
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def login = column[String]("login")
  def firstName = column[String]("first_name")
  def lastName = column[String]("last_name")
  def secondName = column[Option[String]]("second_name")
  def status = column[UserStatus.Value]("status", O.Default(UserStatus.active))

  def uniqueLoginIdx = index("idx_user_login", login, unique = true)

  override def * = (id.?, login, firstName, lastName, secondName, status.?) <> ((User.apply _).tupled, User.unapply)
}
