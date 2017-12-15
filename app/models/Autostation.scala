package models

import play.api.libs.json._
import play.api.libs.functional.syntax._

case class Autostation (id:Long, name: String, cityId: Long, countWorkplaces: Long, address: String,
                        startTime: String, endTime: String, email: String, password: String)

object Autostation {
  implicit val reads: Reads[Autostation] = (
    (JsPath \ "id").read[Long] and
      (JsPath \ "name").read[String] and
      (JsPath \ "cityId").read[Long] and
      (JsPath \ "countWorkplaces").read[Long] and
      (JsPath \ "address").read[String] and
      (JsPath \ "startTime").read[String] and
      (JsPath \ "endTime").read[String] and
      (JsPath \ "email").read[String] and
      (JsPath \ "password").read[String])(Autostation.apply _)

  implicit val write = new Writes[Autostation] {
    def writes(autostation: Autostation): JsValue = {
      Json.obj(
        "id" -> autostation.id,
        "name" -> autostation.name,
        "cityId" -> autostation.cityId,
        "countWorkplaces" -> autostation.countWorkplaces,
        "address" -> autostation.address,
        "startTime" -> autostation.startTime,
        "endTime" -> autostation.endTime,
        "email" -> autostation.email
      )
    }
  }

  def fromJson(success: JsSuccess[Autostation]): Autostation = {
    val id = success.get.id
    val name = success.get.name
    val cityId = success.get.cityId
    val countWorkplaces = success.get.countWorkplaces
    val address = success.get.address
    val startTime = success.get.startTime
    val endTime = success.get.endTime
    val email = success.get.email
    val password = success.get.password

    new Autostation(id, name, cityId, countWorkplaces, address, startTime, endTime, email, password)
  }
}