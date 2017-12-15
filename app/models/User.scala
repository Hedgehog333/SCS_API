package models
import java.sql.Date

import play.api.libs.json._
import play.api.libs.functional.syntax._

case class User (id: Long, fname: String, sname: String, lname: String, email: String,
                 password: String, cityId: Long, birthday: Date, registrationDate: Date, phoneNumber: String)

object User {
  implicit val reads: Reads[User] = (
    (JsPath \ "id").read[Long] and
      (JsPath \ "fname").read[String] and
      (JsPath \ "sname").read[String] and
      (JsPath \ "lname").read[String] and
      (JsPath \ "email").read[String] and
      (JsPath \ "password").read[String] and
      (JsPath \ "cityId").read[Long] and
      (JsPath \ "birthday").read[Date] and
      (JsPath \ "registrationDate").read[Date] and
      (JsPath \ "phoneNumber").read[String])(User.apply _)

  implicit val write = new Writes[User] {
    def writes(user: User): JsValue = {
      Json.obj(
        "id" -> user.id,
        "fname" -> user.fname,
        "sname" -> user.sname,
        "lname" -> user.lname,
        "email" -> user.email,
        "cityId" -> user.cityId,
        "birthday" -> user.birthday.toString,
        "registrationDate" -> user.registrationDate.toString,
        "phoneNumber" -> user.phoneNumber
      )
    }
  }

  def fromJson(success: JsSuccess[User]): User = {
    val id = success.get.id
    val fname = success.get.fname
    val sname = success.get.sname
    val lname = success.get.lname
    val email = success.get.email
    val password = success.get.password
    val cityId = success.get.cityId
    val birthday = success.get.birthday
    val registrationDate = success.get.registrationDate
    val phoneNumber = success.get.phoneNumber

    new User(id, fname, lname, sname, email, password, cityId, birthday, registrationDate, phoneNumber)
  }
}