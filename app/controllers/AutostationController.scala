package controllers

import java.sql._
import javax.inject.Inject

import play.api.libs.json.JsValue
import play.api.mvc.{AbstractController, ControllerComponents}
import services.json.AutostationService

class AutostationController @Inject() (cc: ControllerComponents) extends AbstractController(cc){
  def getById(id: Long) = Action {
    Ok(AutostationService.select(id))
  }

  def setAutostation = Action { implicit request =>
    val bodyAsJson: JsValue = request.body.asJson.get
    Ok(AutostationService.create(bodyAsJson))
  }

  def getAll = Action {
    Ok(AutostationService.select)
  }

  def deleteById(id: Long) = Action {
    Ok(AutostationService.delete(id))
  }

  def update(id: Long) = Action { implicit request =>
    val bodyAsJson: JsValue = request.body.asJson.get
    Ok(AutostationService.update(bodyAsJson, id))
  }
}