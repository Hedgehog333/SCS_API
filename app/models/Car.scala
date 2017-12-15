package models

import java.sql.Date
import play.api.libs.json._
import play.api.libs.functional.syntax._

case class Car (id: Long, vin: String, colorId: Long, modelId: Long, yearOfIssue: Date)

object Car {
  implicit val reads: Reads[Car] = (
    (JsPath \ "id").read[Long] and
      (JsPath \ "vin").read[String] and
      (JsPath \ "colorId").read[Long] and
      (JsPath \ "modelId").read[Long] and
      (JsPath \ "yearOfIssue").read[Date])(Car.apply _)

  implicit val write = new Writes[Car] {
    def writes(user: Car): JsValue = {
      Json.obj(
        "id" -> user.id,
        "vin" -> user.vin,
        "colorId" -> user.colorId,
        "modelId" -> user.modelId,
        "yearOfIssue" -> user.yearOfIssue.toString
      )
    }
  }

  def fromJson(success: JsSuccess[Car]): Car = {
    val id = success.get.id
    val vin = success.get.vin
    val colorId = success.get.colorId
    val modelId = success.get.modelId
    val yearOfIssue = success.get.yearOfIssue

    new Car(id, vin, colorId, modelId, yearOfIssue)
  }
}