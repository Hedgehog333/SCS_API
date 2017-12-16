package models

import play.api.libs.json._
import play.api.libs.functional.syntax._

case class Spare (id: Long, vin: String, typeSpareId: Long, name: String, description: String)

object Spare {
  implicit val reads: Reads[Spare] = (
    (JsPath \ "id").read[Long] and
      (JsPath \ "vin").read[String] and
      (JsPath \ "typeSpareId").read[Long] and
      (JsPath \ "name").read[String] and
      (JsPath \ "description").read[String])(Spare.apply _)

  implicit val write = new Writes[Spare] {
    def writes(spare: Spare): JsValue = {
      Json.obj(
        "id" -> spare.id,
        "vin" -> spare.vin,
        "typeSpareId" -> spare.typeSpareId,
        "name" -> spare.name,
        "description" -> spare.description
      )
    }
  }

  def fromJson(success: JsSuccess[Spare]): Spare = {
    val id = success.get.id
    val vin = success.get.vin
    val typeSpareId = success.get.typeSpareId
    val name = success.get.name
    val description = success.get.description

    new Spare(id, vin, typeSpareId, name, description)
  }
}