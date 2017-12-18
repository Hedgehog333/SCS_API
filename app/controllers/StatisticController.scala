package controllers

import javax.inject.Inject

import play.api.libs.json.JsValue
import play.api.mvc.{AbstractController, ControllerComponents}
import services.json.StatisticService

class StatisticController  @Inject() (cc: ControllerComponents) extends AbstractController(cc){
  def getById(id: Long) = Action {
    Ok(StatisticService.select(id)).withHeaders(headers, origin, methods)
  }

  def setStatistic(userCarId: Long) = Action { implicit request =>
    val bodyAsJson: JsValue = request.body.asJson.get
    Ok(StatisticService.create(bodyAsJson, userCarId)).withHeaders(headers, origin, methods)
  }

  def getSpareStatistic(userCarId: Long, detailNumber: Long) = Action {
    Ok(StatisticService.select(userCarId, detailNumber)).withHeaders(headers, origin, methods)
  }

  def deleteById(userCarId: Long, detailNumber: Long) = Action {
    Ok(StatisticService.delete(userCarId, detailNumber)).withHeaders(headers, origin, methods)
  }

  def update(userCarId: Long, detailNumber: Long) = Action { implicit request =>
    val bodyAsJson: JsValue = request.body.asJson.get
    Ok(StatisticService.update(bodyAsJson, userCarId, detailNumber)).withHeaders(headers, origin, methods)
  }

  def headers = (ACCESS_CONTROL_ALLOW_HEADERS -> "Origin, Content-Type, Accept")
  def origin = (ACCESS_CONTROL_ALLOW_ORIGIN -> "*")
  def methods = (ACCESS_CONTROL_ALLOW_METHODS -> "POST, GET, PUT, DELETE")
}