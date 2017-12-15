package services.json

import dao.Cars
import models.{Car, ReturnedJson}
import play.api.libs.json._

import scala.util.{Failure, Success}

object CarService extends Service[JsValue] {
  override def create(jsonCar: JsValue): JsValue = {
    jsonCar.validate[Car] match {
      case success: JsSuccess[Car] => {
        Cars.create(Car.fromJson(success)) match {
          case Success(s) => ReturnedJson.ok("Car created!", Json.obj("id" -> s.toLong))
          case Failure(e) => ReturnedJson.internalServerError("Error with create car! " + e.getMessage)
        }
      }
      case JsError(error) => ReturnedJson.bedRequest
    }
  }

  override def select: JsValue = {
    Cars.getAll() match {
      case Success(v) => ReturnedJson.ok("All cars!", Json.toJson(v))
      case Failure(e) => ReturnedJson.internalServerError("Error with return all cars! " + e.getMessage)
    }
  }

  override def select(id: Long): JsValue = {
    Cars.getById(id) match {
      case Success(v) => ReturnedJson.ok("Car created!", Json.toJson(v))
      case Failure(e) => ReturnedJson.internalServerError("Error with create car! " + e.getMessage)
    }
  }

  override def update(jsonCar: JsValue, id: Long): JsValue = {
    jsonCar.validate[Car] match {
      case success: JsSuccess[Car] => {
        val car: Car = Car.fromJson(success)
        if(car.id == id)
        Cars.update(car) match {
            case Success(s) => ReturnedJson.ok(if(s) "Car updated!" else s"Car id=$id not found!")
            case Failure(e) => ReturnedJson.internalServerError("Error with update car! " + e.getMessage)
          }
        else
        ReturnedJson.bedRequest("You send inconnect data! ID in url not equals for you sending data.")
      }
      case JsError(error) => ReturnedJson.bedRequest
    }
  }

  override def delete(id: Long): JsValue = {
    Cars.delete(id) match {
      case Success(s) => ReturnedJson.ok("Car deleted!")
      case Failure(e) => ReturnedJson.internalServerError(s"Error with delete car id=$id! " + e.getMessage)
    }
  }
}
