package controllers

import javax.inject.Inject

import play.api.libs.json.JsValue
import play.api.mvc.{AbstractController, ControllerComponents}
import services.json.CarService

class CarController @Inject() (cc: ControllerComponents) extends AbstractController(cc){
  def getById(id: Long) = Action {
    Ok(CarService.select(id))
  }

  def setCar = Action { implicit request =>
    val bodyAsJson: JsValue = request.body.asJson.get
    Ok(CarService.create(bodyAsJson))
  }

  def getAll = Action {
    Ok(CarService.select)
  }

  def deleteById(id: Long) = Action {
    Ok(CarService.delete(id))
  }

  def update(id: Long) = Action { implicit request =>
    val bodyAsJson: JsValue = request.body.asJson.get
    Ok(CarService.update(bodyAsJson, id))
  }
}