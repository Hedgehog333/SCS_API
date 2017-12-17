package services.json

import dao.Statistics
import models.{ReturnedJson, Statistic}
import play.api.libs.json.{JsError, JsSuccess, JsValue, Json}
import scala.util.{Failure, Success}

object StatisticService {
  def create(jsonStatistic: JsValue, userCarId: Long): JsValue = {
    jsonStatistic.validate[Statistic] match {
      case success: JsSuccess[Statistic] => {
        val statistic = Statistic.fromJson(success)

        if(statistic.userCarId == userCarId)
          Statistics.create(statistic) match {
            case Success(s) => ReturnedJson.ok("Statistic created!", Json.obj("id" -> s.toLong))
            case Failure(e) => ReturnedJson.internalServerError("Error with create Statistic! " + e.getMessage)
          }
        else
          ReturnedJson.bedRequest("You send inconnect data! Data in url not equals for you sending data.")
      }
      case JsError(error) => ReturnedJson.bedRequest
    }
  }

  def select(userCarId: Long, detailNumber: Long): JsValue = {
    Statistics.getStatCarSpare(userCarId, detailNumber) match {
      case Success(v) => ReturnedJson.ok("Statistic returned!", Json.toJson(v))
      case Failure(e) => ReturnedJson.internalServerError("Error with returned statistic! " + e.getMessage)
    }
  }

  def select(userCarId: Long): JsValue = {
    Statistics.getAll(userCarId) match {
      case Success(v) => ReturnedJson.ok("Statistic returned!", Json.toJson(v))
      case Failure(e) => ReturnedJson.internalServerError("Error with returned statistic! " + e.getMessage)
    }
  }

  def update(jsonStatistic: JsValue, userCarId: Long, detailNumber: Long): JsValue = {
    jsonStatistic.validate[Statistic] match {
      case success: JsSuccess[Statistic] => {
        val statistic: Statistic = Statistic.fromJson(success)
        if(statistic.userCarId == userCarId && statistic.detailNumber == detailNumber)
          Statistics.update(statistic) match {
            case Success(s) => ReturnedJson.ok(if(s) "Statistic updated!" else "Statistic not found!")
            case Failure(e) => ReturnedJson.internalServerError("Error with update statistic! " + e.getMessage)
          }
        else
          ReturnedJson.bedRequest("You send inconnect data! Data in url not equals for you sending data.")
      }
      case JsError(error) => ReturnedJson.bedRequest
    }
  }

  def delete(userId: Long, carId: Long): JsValue = {
    Statistics.delete(userId, carId) match {
      case Success(s) => ReturnedJson.ok("Statistic deleted!")
      case Failure(e) => ReturnedJson.internalServerError(s"Error with delete statistic, car id=$carId! " + e.getMessage)
    }
  }
}