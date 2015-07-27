package controllers

import model.{User, MockDB}
import play.api.mvc._
import play.api.libs.json._
import model.JsonParsers.userFormatter

import scala.util.{Success, Failure}

/**
 * Created by adminuser on 7/27/15.
 */

class WorkingWithJson extends Controller {

  def allUsers = Action {
    Ok(Json.toJson(MockDB.users))
  }

  def addUser = Action(parse.json[User]) { implicit request =>
    MockDB.addUser(request.body) match {
      case Success(user) =>
        Ok(Json.toJson(user))
      case Failure(error) =>
        BadRequest(error.getMessage)
    }
  }

  def getUser(name : String) = Action {
    MockDB.getUser(name) match {
      case Success(user) =>
        Ok(Json.toJson(user))
      case Failure(error) =>
        BadRequest(error.getMessage)
    }
  }

}
