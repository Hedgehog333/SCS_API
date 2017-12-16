package models

import java.sql.Date
import play.api.libs.json._
import play.api.libs.functional.syntax._

case class Queue (id: Long, carId: Long, autostationId: Long, date: Date, time: String, lenhthTime: String, workplaceNumber: Long, status: Long)

object Queue {
  implicit val reads: Reads[Queue] = (
    (JsPath \ "id").read[Long] and
      (JsPath \ "carId").read[Long] and
      (JsPath \ "autostationId").read[Long] and
      (JsPath \ "date").read[Date] and
      (JsPath \ "time").read[String] and
      (JsPath \ "lenhthTime").read[String] and
      (JsPath \ "workplaceNumber").read[Long] and
      (JsPath \ "status").read[Long])(Queue.apply _)

  implicit val write = new Writes[Queue] {
    def writes(queue: Queue): JsValue = {
      Json.obj(
        "id" -> queue.id,
        "carId" -> queue.carId,
        "autostationId" -> queue.autostationId,
        "date" -> queue.date.toString,
        "time" -> queue.time.toString,
        "lenhthTime" -> queue.lenhthTime.toString,
        "workplaceNumber" -> queue.workplaceNumber,
        "status" -> queue.status
      )
    }
  }

  def fromJson(success: JsSuccess[Queue]): Queue = {
    val id = success.get.id
    val carId = success.get.carId
    val autostationId = success.get.autostationId
    val date = success.get.date
    val time = success.get.time
    val lenhthTime = success.get.lenhthTime
    val workplaceNumber = success.get.workplaceNumber
    val status = success.get.status

    new Queue(id, carId, autostationId, date, time, lenhthTime, workplaceNumber, status)
  }
}