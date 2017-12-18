package controllers

import javax.inject.Inject

import models.ReturnedJson
import play.api.libs.json.JsValue
import play.api.mvc.{AbstractController, ControllerComponents}
import services.json.LoginService

class LoginController @Inject() (cc: ControllerComponents) extends AbstractController(cc) {
  def signIn(role: String) = Action { request =>
    val bodyAsJson: JsValue = request.body.asJson.get
    val result = LoginService.login(bodyAsJson, role)

    Ok(result._2).withHeaders(headers, origin, methods)
  }

  def headers = (ACCESS_CONTROL_ALLOW_HEADERS -> "Origin, Content-Type, Accept")
  def origin = (ACCESS_CONTROL_ALLOW_ORIGIN -> "*")
  def methods = (ACCESS_CONTROL_ALLOW_METHODS -> "POST, GET, PUT, DELETE")
}