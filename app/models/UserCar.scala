package models

import java.sql.Date
import play.api.libs.json._
import play.api.libs.functional.syntax._

case class UserCar(id: Long, userId: Long, carId: Long, attachDate: Date)

object UserCar {
  implicit val reads: Reads[UserCar] = (
    (JsPath \ "id").read[Long] and
      (JsPath \ "userId").read[Long] and
      (JsPath \ "carId").read[Long] and
      (JsPath \ "attachDate").read[Date])(UserCar.apply _)

  implicit val write = new Writes[UserCar] {
    def writes(user: UserCar): JsValue = {
      Json.obj(
        "id" -> user.id,
        "userId" -> user.userId,
        "carId" -> user.carId,
        "attachDate" -> user.attachDate.toString
      )
    }
  }

  def fromJson(success: JsSuccess[UserCar]): UserCar = {
    val id = success.get.id
    val userId = success.get.userId
    val carId = success.get.carId
    val attachDate = success.get.attachDate

    new UserCar(id, userId, carId, attachDate)
  }
}