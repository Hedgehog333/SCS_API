package models

import play.api.libs.json._
import play.api.libs.functional.syntax._

case class Statistic (id: Long, detailNumber: Long, spareId: Long, userCarId: Long, state: Long, typeSpareId: Long, seal: Long)

object Statistic {
  implicit val reads: Reads[Statistic] = (
    (JsPath \ "id").read[Long] and
      (JsPath \ "detailNumber").read[Long] and
      (JsPath \ "spareId").read[Long] and
      (JsPath \ "userCarId").read[Long] and
      (JsPath \ "state").read[Long] and
      (JsPath \ "typeSpareId").read[Long] and
      (JsPath \ "seal").read[Long])(Statistic.apply _)

  implicit val write = new Writes[Statistic] {
    def writes(statistic: Statistic): JsValue = {
      Json.obj(
        "id" -> statistic.id,
        "detailNumber" -> statistic.detailNumber,
        "spareId" -> statistic.spareId,
        "userCarId" -> statistic.userCarId,
        "state" -> statistic.state,
        "typeSpareId" -> statistic.typeSpareId,
        "seal" -> statistic.seal
      )
    }
  }

  def fromJson(success: JsSuccess[Statistic]): Statistic = {
    val id = success.get.id
    val detailNumber = success.get.detailNumber
    val spareId = success.get.spareId
    val userCarId = success.get.userCarId
    val state = success.get.state
    val typeSpareId = success.get.typeSpareId
    val seal = success.get.seal

    new Statistic(id, detailNumber, spareId, userCarId, state, typeSpareId,seal)
  }
}