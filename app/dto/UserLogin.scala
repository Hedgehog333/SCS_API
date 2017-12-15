package dto

import play.api.libs.json._
import play.api.libs.functional.syntax._

case class UserLogin (email: String, password: String)

object UserLogin {
  implicit val reads: Reads[UserLogin] = (
    (JsPath \ "email").read[String] and
      (JsPath \ "password").read[String])(UserLogin.apply _)

  def fromJson(success: JsSuccess[UserLogin]): UserLogin = {
    val email = success.get.email
    val password = success.get.password

    new UserLogin(email, password)
  }
}