package dao

import scala.util.Try

trait DAO[T] {
  def create(obj: T): Try[Long]
  def getById(id: Long): Try[T]
  def getAll(): Try[Set[T]]
  def update(obj: T): Try[Boolean]
  def delete(id: Long): Try[Boolean]
}