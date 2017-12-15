package dto

import play.api.libs.json.{JsPath, JsSuccess, Reads}
import play.api.libs.functional.syntax._

case class AutostationLogin (email: String, password: String)

object AutostationLogin {
  implicit val reads: Reads[AutostationLogin] = (
    (JsPath \ "email").read[String] and
      (JsPath \ "password").read[String])(AutostationLogin.apply _)

  def fromJson(success: JsSuccess[AutostationLogin]): AutostationLogin = {
    val email = success.get.email
    val password = success.get.password

    new AutostationLogin(email, password)
  }
}