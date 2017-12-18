package controllers

import javax.inject.Inject
import java.sql.Date

import play.api.libs.json.JsValue
import play.api.mvc.{AbstractController, ControllerComponents}
import services.json.QueueService

import scala.util.{Failure, Success, Try}

class QueueController @Inject() (cc: ControllerComponents) extends AbstractController(cc){
  def getAllQueues(id: Long, date: String) = Action {
    Try(Date.valueOf(date))  match {
      case Success(s) => Ok(QueueService.selectQueues(id, s.asInstanceOf[Date])).withHeaders(headers, origin, methods)
      case Failure(e) => BadRequest(e.toString).withHeaders(headers, origin, methods)
    }
  }

  def getAllCars(id: Long, date: String) = Action {
    Try(Date.valueOf(date))  match {
      case Success(s) => Ok(QueueService.selectCars(id, s.asInstanceOf[Date])).withHeaders(headers, origin, methods)
      case Failure(e) => BadRequest(e.toString).withHeaders(headers, origin, methods)
    }
  }

  def getCar(id: Long, date: String, userCarId: Long) = Action {
    Try(Date.valueOf(date))  match {
      case Success(s) => Ok(QueueService.selectCar(id, s.asInstanceOf[Date], userCarId)).withHeaders(headers, origin, methods)
      case Failure(e) => BadRequest(e.toString).withHeaders(headers, origin, methods)
    }
  }

  def update(id: Long, date: String, userCarId: Long) = Action { implicit request =>
    val bodyAsJson: JsValue = request.body.asJson.get
    Try(Date.valueOf(date))  match {
      case Success(s) => Ok(QueueService.update(bodyAsJson, id, s.asInstanceOf[Date], userCarId)).withHeaders(headers, origin, methods)
      case Failure(e) => BadRequest(e.toString).withHeaders(headers, origin, methods)
    }
  }

  def delete(id: Long, date: String, userCarId: Long) = Action {
    Try(Date.valueOf(date))  match {
      case Success(s) => Ok(QueueService.delete(id, s.asInstanceOf[Date], userCarId)).withHeaders(headers, origin, methods)
      case Failure(e) => BadRequest(e.toString).withHeaders(headers, origin, methods)
    }
  }

  def create(id: Long, date: String, userCarId: Long) = Action { implicit request =>
    val bodyAsJson: JsValue = request.body.asJson.get
    Try(Date.valueOf(date))  match {
      case Success(s) => Ok(QueueService.create(bodyAsJson)).withHeaders(headers, origin, methods)
      case Failure(e) => BadRequest(e.toString).withHeaders(headers, origin, methods)
    }
  }

  def headers = (ACCESS_CONTROL_ALLOW_HEADERS -> "Origin, Content-Type, Accept")
  def origin = (ACCESS_CONTROL_ALLOW_ORIGIN -> "*")
  def methods = (ACCESS_CONTROL_ALLOW_METHODS -> "POST, GET, PUT, DELETE")
}