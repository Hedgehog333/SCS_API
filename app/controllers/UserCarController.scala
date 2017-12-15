package controllers

import javax.inject.Inject
import services.json.UserCarService
import play.api.libs.json.JsValue
import play.api.mvc.{AbstractController, ControllerComponents}

class UserCarController @Inject() (cc: ControllerComponents) extends AbstractController(cc){

  def getAllCarsUser(userId: Long) = Action {
    Ok(UserCarService.select(userId))
  }

  def getByUserIdAndCarId(userId: Long, carId: Long) = Action {
    Ok(UserCarService.select(userId, carId))
  }

  def setUserCar(userId: Long, carId: Long) = Action { implicit request =>
    val bodyAsJson: JsValue = request.body.asJson.get
    Ok(UserCarService.create(bodyAsJson))
  }

  def deleteByUserIdAndCarId(userId: Long, carId: Long) = Action {
    Ok(UserCarService.deleteCar(userId, carId))
  }
}