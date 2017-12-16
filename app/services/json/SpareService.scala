package services.json

import models.{ReturnedJson, Spare}
import dao.Spares
import play.api.libs.json.{JsError, JsSuccess, JsValue, Json}
import scala.util.{Failure, Success}

object SpareService extends Service[JsValue] {
  override def create(jsonCar: JsValue): JsValue = {
    jsonCar.validate[Spare] match {
      case success: JsSuccess[Spare] => {
        Spares.create(Spare.fromJson(success)) match {
          case Success(s) => ReturnedJson.ok("Spare created!", Json.obj("id" -> s.toLong))
          case Failure(e) => ReturnedJson.internalServerError("Error with create Spare! " + e.getMessage)
        }
      }
      case JsError(error) => ReturnedJson.bedRequest
    }
  }

  override def select: JsValue = {
    Spares.getAll() match {
      case Success(v) => ReturnedJson.ok("All spares!", Json.toJson(v))
      case Failure(e) => ReturnedJson.internalServerError("Error with return all spares! " + e.getMessage)
    }
  }

  override def select(id: Long): JsValue = {
    Spares.getById(id) match {
      case Success(v) => ReturnedJson.ok("Spare created!", Json.toJson(v))
      case Failure(e) => ReturnedJson.internalServerError("Error with create spare! " + e.getMessage)
    }
  }

  override def update(jsonCar: JsValue, id: Long): JsValue = {
    jsonCar.validate[Spare] match {
      case success: JsSuccess[Spare] => {
        val spare: Spare = Spare.fromJson(success)
        if(spare.id == id)
          Spares.update(spare) match {
            case Success(s) => ReturnedJson.ok(if(s) "Spare updated!" else s"Spare id=$id not found!")
            case Failure(e) => ReturnedJson.internalServerError("Error with update spare! " + e.getMessage)
          }
        else
          ReturnedJson.bedRequest("You send inconnect data! ID in url not equals for you sending data.")
      }
      case JsError(error) => ReturnedJson.bedRequest
    }
  }

  override def delete(id: Long): JsValue = {
    Spares.delete(id) match {
      case Success(s) => ReturnedJson.ok("Spare deleted!")
      case Failure(e) => ReturnedJson.internalServerError(s"Error with delete spare id=$id! " + e.getMessage)
    }
  }
}