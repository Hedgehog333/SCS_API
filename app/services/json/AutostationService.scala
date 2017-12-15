package services.json

import models.{ReturnedJson, Autostation}
import dao.Autostations
import play.api.libs.json.{JsError, JsSuccess, JsValue, Json}

import scala.util.{Failure, Success}

object AutostationService extends Service[JsValue] {
  override def create(jsonAutostation: JsValue): JsValue = {
    jsonAutostation.validate[Autostation] match {
      case success: JsSuccess[Autostation] => Autostations.create(Autostation.fromJson(success)) match {
        case Success(s) => ReturnedJson.ok("Autostation created!", Json.obj("id" -> s.toLong))
        case Failure(e) => ReturnedJson.internalServerError("Error with create autostation! " + e.getMessage)
      }
      case JsError(error) => ReturnedJson.bedRequest
    }
  }

  override def select: JsValue = {
    Autostations.getAll() match {
      case Success(v) => ReturnedJson.ok("All autostation!", Json.toJson(v))
      case Failure(e) => ReturnedJson.internalServerError("Error with return all autostation! " + e.getMessage)
    }
  }

  override def select(id: Long): JsValue = {
    Autostations.getById(id) match {
      case Success(v) => ReturnedJson.ok("Autostation created!", Json.toJson(v))
      case Failure(e) => ReturnedJson.internalServerError("Error with create autostation! " + e.getMessage)
    }
  }

  override def update(jsonAutostation: JsValue, id: Long): JsValue = {
    jsonAutostation.validate[Autostation] match {
      case success: JsSuccess[Autostation] => {
        val user: Autostation = Autostation.fromJson(success)
        if(user.id == id)
          Autostations.update(user) match {
            case Success(s) => ReturnedJson.ok(if(s) "Autostation updated!" else s"Autostation id=$id not found!")
            case Failure(e) => ReturnedJson.internalServerError("Error with update autostation! " + e.getMessage)
          }
        else
          ReturnedJson.bedRequest("You send inconnect data! ID in url not equals for you sending data.")
      }
      case JsError(error) => ReturnedJson.bedRequest("You send inconnect data!")
    }
  }

  override def delete(id: Long): JsValue = {
    Autostations.delete(id) match {
      case Success(s) => ReturnedJson.ok("Autostation deleted!")
      case Failure(e) => ReturnedJson.internalServerError(s"Error with delete Autostation id=$id! " + e.getMessage)
    }
  }
}