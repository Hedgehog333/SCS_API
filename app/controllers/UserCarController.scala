package controllers

import javax.inject.Inject
import services.json.UserCarService
import play.api.libs.json.JsValue
import play.api.mvc.{AbstractController, ControllerComponents}

class UserCarController @Inject() (cc: ControllerComponents) extends AbstractController(cc){

  def getAllCarsUser(userId: Long) = Action {
    Ok(UserCarService.select(userId)).withHeaders(headers, origin, methods)
  }

  def getByUserIdAndCarId(userId: Long, carId: Long) = Action {
    Ok(UserCarService.select(userId, carId)).withHeaders(headers, origin, methods)
  }

  def setUserCar(userId: Long, carId: Long) = Action { implicit request =>
    val bodyAsJson: JsValue = request.body.asJson.get
    Ok(UserCarService.create(bodyAsJson)).withHeaders(headers, origin, methods)
  }

  def deleteByUserIdAndCarId(userId: Long, carId: Long) = Action {
    Ok(UserCarService.deleteCar(userId, carId)).withHeaders(headers, origin, methods)
  }

  def headers = (ACCESS_CONTROL_ALLOW_HEADERS -> "Origin, Content-Type, Accept")
  def origin = (ACCESS_CONTROL_ALLOW_ORIGIN -> "*")
  def methods = (ACCESS_CONTROL_ALLOW_METHODS -> "POST, GET, PUT, DELETE")
}