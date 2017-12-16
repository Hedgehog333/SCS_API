package controllers

import javax.inject.Inject

import play.api.libs.json.JsValue
import play.api.mvc.{AbstractController, ControllerComponents}
import services.json.SpareService

class SpareController  @Inject() (cc: ControllerComponents) extends AbstractController(cc){
  def getById(id: Long) = Action {
    Ok(SpareService.select(id))
  }

  def setSpare = Action { implicit request =>
    val bodyAsJson: JsValue = request.body.asJson.get
    Ok(SpareService.create(bodyAsJson))
  }

  def getAll = Action {
    Ok(SpareService.select)
  }

  def deleteById(id: Long) = Action {
    Ok(SpareService.delete(id))
  }

  def update(id: Long) = Action { implicit request =>
    val bodyAsJson: JsValue = request.body.asJson.get
    Ok(SpareService.update(bodyAsJson, id))
  }
}