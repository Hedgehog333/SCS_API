package services.json

trait Service[T] {
  def create(obj: T): T
  def select: T
  def select(id: Long): T
  def update(obj: T, id: Long): T
  def delete(id: Long): T
}