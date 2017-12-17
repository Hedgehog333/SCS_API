package services.json

import dao.UserCars
import models.{UserCar, ReturnedJson}
import play.api.libs.json.{JsError, JsSuccess, JsValue, Json}

import scala.util.{Failure, Success}

object UserCarService {
  def create(jsonUserCar: JsValue): JsValue = {
    jsonUserCar.validate[UserCar] match {
      case success: JsSuccess[UserCar] => UserCars.create(UserCar.fromJson(success)) match {
        case Success(s) => ReturnedJson.ok("User car created!", Json.obj("id" -> s.toLong))
        case Failure(e) => ReturnedJson.internalServerError("Error with create user car! " + e.getMessage)
      }
      case JsError(error) => ReturnedJson.bedRequest
    }
  }

  def select(userId: Long): JsValue = {
    UserCars.getAll(userId) match {
      case Success(v) => ReturnedJson.ok("User cars returned!", Json.toJson(v))
      case Failure(e) => ReturnedJson.internalServerError("Error with returned user car! " + e.getMessage)
    }
  }

  def select(userId: Long, carId: Long): JsValue = {
    UserCars.getById(userId, carId) match {
      case Success(v) => ReturnedJson.ok("User car returned!", Json.toJson(v))
      case Failure(e) => ReturnedJson.internalServerError("Error with returned user car! " + e.getMessage)
    }
  }

  def deleteCar(userId: Long, carId: Long): JsValue = {
    UserCars.deleteCar(userId, carId) match {
      case Success(s) => ReturnedJson.ok("User car deleted!")
      case Failure(e) => ReturnedJson.internalServerError(s"Error with delete user, car id=$carId! " + e.getMessage)
    }
  }
}
