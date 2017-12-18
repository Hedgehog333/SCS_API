package services.json

import play.api.libs.json._
import scala.util.{Failure, Success, Try}
import dto.{UserLogin, AutostationLogin}
import dao.{Users, Autostations}
import models.{ReturnedJson, User, Autostation}

object LoginService {
  def login(jsonUser: JsValue, role: String): Pair[Option[Any], JsValue] = {
    role match {
      case "user" => loginUser(jsonUser, role)
      case "autostation" => loginAutostation(jsonUser, role)
      case _ => Pair(None, ReturnedJson.bedRequest)
    }
  }

  private def loginUser(value: JsValue, role: String): Pair[Option[User], JsValue] = {
    value.validate[UserLogin] match {
      case success: JsSuccess[UserLogin] => {
        Users.getByEmailAndPassword(UserLogin.fromJson(success)) match {
          case Success(s) => Pair(Some(s), ReturnedJson.ok("Login success!", Json.toJson(s)))
          case Failure(e) => Pair(None, ReturnedJson.notFound("Login error!"))
        }
      }
      case JsError(error) => Pair(None, ReturnedJson.bedRequest)
    }
  }

  private def loginAutostation(value: JsValue, role: String): Pair[Option[Autostation], JsValue] = {
    value.validate[AutostationLogin] match {
      case success: JsSuccess[AutostationLogin] => {
        Autostations.getByEmailAndPassword(AutostationLogin.fromJson(success)) match {
          case Success(s) => Pair(Some(s), ReturnedJson.ok("Login success!", Json.toJson(s)))
          case Failure(e) => Pair(None, ReturnedJson.notFound("Login error!"))
        }
      }
      case JsError(error) => Pair(None, ReturnedJson.bedRequest)
    }
  }
}
