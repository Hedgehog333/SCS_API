package services.json

import java.sql.Date

import dao.Queues
import models.{Queue, ReturnedJson}
import play.api.libs.json._

import scala.util.{Failure, Success}

object QueueService {
  def create(jsonQueue: JsValue): JsValue = {
    jsonQueue.validate[Queue] match {
      case success: JsSuccess[Queue] => {
        Queues.create(Queue.fromJson(success)) match {
          case Success(s) => ReturnedJson.ok("Queue created!", Json.obj("id" -> s.toLong))
          case Failure(e) => ReturnedJson.internalServerError("Error with create Queue! " + e.getMessage)
        }
      }
      case JsError(error) => ReturnedJson.bedRequest
    }
  }

  def selectQueues(autostatuinId: Long, date: Date): JsValue = {
    Queues.getAll(autostatuinId, date) match {
      case Success(v) => ReturnedJson.ok("All queues!", Json.toJson(v))
      case Failure(e) => ReturnedJson.internalServerError("Error with return all queue! " + e.getMessage)
    }
  }

  def selectCars(autostatuinId: Long, date: Date): JsValue = {
    Queues.getCarsByDate(autostatuinId, date) match {
      case Success(v) => ReturnedJson.ok(s"Cars in queue, date ${date.toString}!", Json.toJson(v))
      case Failure(e) => ReturnedJson.internalServerError("Error with create car! " + e.getMessage)
    }
  }

  def selectCar(autostatuinId: Long, date: Date, carId: Long): JsValue = {
    Queues.getCarByDate(autostatuinId, date, carId) match {
      case Success(v) => ReturnedJson.ok(s"Car in queue, date ${date.toString}!", Json.toJson(v))
      case Failure(e) => ReturnedJson.internalServerError("Error with select car! " + e.getMessage)
    }
  }

  def update(jsonQueue: JsValue, autostatuinId: Long, date: Date, carId: Long): JsValue = {
    jsonQueue.validate[Queue] match {
      case success: JsSuccess[Queue] => {
        val queie: Queue = Queue.fromJson(success)
        if(queie.autostationId == autostatuinId && queie.date.equals(date) && queie.carId == carId)
          Queues.update(queie) match {
            case Success(s) => ReturnedJson.ok(if(s) "Queue updated!" else s"Queue not found!")
            case Failure(e) => ReturnedJson.internalServerError("Error with update car! " + e.getMessage)
          }
        else
          ReturnedJson.bedRequest("You send inconnect data! Url not equals for you sending data.")
      }
      case JsError(error) => ReturnedJson.bedRequest
    }
  }

  def delete(autostationId: Long, date: Date, userCarId: Long): JsValue = {
    Queues.delete(autostationId, date, userCarId) match {
      case Success(s) => ReturnedJson.ok("Queue deleted! " + s)
      case Failure(e) => ReturnedJson.internalServerError(s"Error with delete queue! " + e.getMessage)
    }
  }
}