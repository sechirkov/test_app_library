package com.sch.library.dao

import scala.concurrent.Future

/**
 * User: schirkov
 * Date: 9/13/2016
 */
trait GenericDao[T] {
  type ID
  def find(id: ID): Future[Option[T]]
  def findAll(): Future[Seq[T]]
  def delete(entity: T): Future[Boolean]
  def persist(entity: T): Future[T]
  def update(entity: T): Future[Boolean]
}
