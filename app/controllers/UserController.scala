package controllers

import javax.inject.Inject

import play.api.libs.json._
import play.api.mvc.{AbstractController, ControllerComponents}
import services.json.UserService

class UserController @Inject() (cc: ControllerComponents) extends AbstractController(cc) {
  def getById(id: Long) = Action {
    Ok(UserService.select(id))
  }

  def setUser = Action { implicit request =>
    val bodyAsJson: JsValue = request.body.asJson.get
    Ok(UserService.create(bodyAsJson))
  }

  def getAll = Action {
    Ok(UserService.select)
  }

  def deleteById(id: Long) = Action {
    Ok(UserService.delete(id))
  }

  def update(id: Long) = Action { implicit request =>
    val bodyAsJson: JsValue = request.body.asJson.get
    Ok(UserService.update(bodyAsJson, id))
  }
}