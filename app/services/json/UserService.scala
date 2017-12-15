package services.json

import dao.Users
import models.{ReturnedJson, User}
import play.api.libs.json._

import scala.util.{Failure, Success}

object UserService extends Service[JsValue]{
  override def create(jsonUser: JsValue): JsValue = {
    jsonUser.validate[User] match {
      case success: JsSuccess[User] => Users.create(User.fromJson(success)) match {
        case Success(s) => ReturnedJson.ok("User created!", Json.obj("id" -> s.toLong))
        case Failure(e) => ReturnedJson.internalServerError("Error with create user! " + e.getMessage)
      }
      case JsError(error) => ReturnedJson.bedRequest
    }
  }

  override def select: JsValue = {
    Users.getAll() match {
      case Success(v) => ReturnedJson.ok("All users!", Json.toJson(v))
      case Failure(e) => ReturnedJson.internalServerError("Error with return all users! " + e.getMessage)
    }
  }

  override def select(id: Long): JsValue = {
    Users.getById(id) match {
      case Success(v) => ReturnedJson.ok("User created!", Json.toJson(v))
      case Failure(e) => ReturnedJson.internalServerError("Error with create user! " + e.getMessage)
    }
  }

  override def update(jsonUser: JsValue, id: Long): JsValue = {
    jsonUser.validate[User] match {
      case success: JsSuccess[User] => {
        val user: User = User.fromJson(success)
        if(user.id == id)
        Users.update(user) match {
            case Success(s) => ReturnedJson.ok(if(s) "User updated!" else s"User id=$id not found!")
            case Failure(e) => ReturnedJson.internalServerError("Error with update user! " + e.getMessage)
          }
        else
        ReturnedJson.bedRequest("You send inconnect data! ID in url not equals for you sending data.")
      }
      case JsError(error) => ReturnedJson.bedRequest("You send inconnect data!")
    }
  }

  override def delete(id: Long): JsValue = {
    Users.delete(id) match {
      case Success(s) => ReturnedJson.ok("User deleted!")
      case Failure(e) => ReturnedJson.internalServerError(s"Error with delete user id=$id! " + e.getMessage)
    }
  }
}