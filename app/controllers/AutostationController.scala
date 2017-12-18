package controllers

import java.sql._
import javax.inject.Inject

import play.api.libs.json.JsValue
import play.api.mvc.{AbstractController, ControllerComponents}
import services.json.AutostationService

class AutostationController @Inject() (cc: ControllerComponents) extends AbstractController(cc){
  def getById(id: Long) = Action {
    Ok(AutostationService.select(id)).withHeaders(headers, origin, methods)
  }

  def setAutostation = Action { implicit request =>
    val bodyAsJson: JsValue = request.body.asJson.get
    Ok(AutostationService.create(bodyAsJson)).withHeaders(headers, origin, methods)
  }

  def getAll = Action {
    Ok(AutostationService.select).withHeaders(headers, origin, methods)
  }

  def deleteById(id: Long) = Action {
    Ok(AutostationService.delete(id)).withHeaders(headers, origin, methods)
  }

  def update(id: Long) = Action { implicit request =>
    val bodyAsJson: JsValue = request.body.asJson.get
    Ok(AutostationService.update(bodyAsJson, id)).withHeaders(headers, origin, methods)
  }

  def headers = (ACCESS_CONTROL_ALLOW_HEADERS -> "Origin, Content-Type, Accept")
  def origin = (ACCESS_CONTROL_ALLOW_ORIGIN -> "*")
  def methods = (ACCESS_CONTROL_ALLOW_METHODS -> "POST, GET, PUT, DELETE")
}