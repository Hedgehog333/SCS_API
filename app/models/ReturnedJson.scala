package models

import play.api.libs.json.{JsValue, Json}
object ReturnedJson {
  def ok(message: String): JsValue = {
    Json.obj(
      "status" -> "200",
      "label" -> "OK",
      "message" -> message
    )
  }

  def ok(message: String, data: JsValue): JsValue = {
    Json.obj(
      "status" -> "200",
      "label" -> "OK",
      "message" -> message,
      "data" -> data
    )
  }

  def bedRequest: JsValue = {
    Json.obj(
      "status" -> "400",
      "label" -> "Bad Request",
      "message" -> "You send inconnect data!"
    )
  }

  def bedRequest(message: String): JsValue = {
    Json.obj(
      "status" -> "400",
      "label" -> "Bad Request",
      "message" -> message
    )
  }

  def notFound(message: String): JsValue = {
    Json.obj(
      "status" -> "404",
      "label" -> "Not Found",
      "message" -> message
    )
  }

  def internalServerError(message: String): JsValue = {
    Json.obj(
      "status" -> "500",
      "label" -> "Internal server error",
      "message" -> message
    )
  }
}