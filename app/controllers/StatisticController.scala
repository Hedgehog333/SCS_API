package controllers

import javax.inject.Inject

import play.api.libs.json.JsValue
import play.api.mvc.{AbstractController, ControllerComponents}
import services.json.StatisticService

class StatisticController  @Inject() (cc: ControllerComponents) extends AbstractController(cc){
  def getById(id: Long) = Action {
    Ok(StatisticService.select(id))
  }

  def setStatistic(userCarId: Long) = Action { implicit request =>
    val bodyAsJson: JsValue = request.body.asJson.get
    Ok(StatisticService.create(bodyAsJson, userCarId))
  }

  def getSpareStatistic(userCarId: Long, detailNumber: Long) = Action {
    Ok(StatisticService.select(userCarId, detailNumber))
  }

  def deleteById(userCarId: Long, detailNumber: Long) = Action {
    Ok(StatisticService.delete(userCarId, detailNumber))
  }

  def update(userCarId: Long, detailNumber: Long) = Action { implicit request =>
    val bodyAsJson: JsValue = request.body.asJson.get
    Ok(StatisticService.update(bodyAsJson, userCarId, detailNumber))
  }
}