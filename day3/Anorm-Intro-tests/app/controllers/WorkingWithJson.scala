package controllers

/*
Copyright [2015] [Dario A Lencina Talarico]

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0
Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations
 under the License.
 */

import model.User
import model.persistance.{AnormDAO, DAO, MockDAO}
import play.api.libs.json._
import play.api.mvc._
import scala.reflect._
import scala.util.{Failure, Success}
import model.JsonParsers._

class WorkingWithJson extends Controller {

  val DAO : DAO = AnormDAO

  def allUsers = Action {
    Ok(Json.toJson(DAO.users))
  }

  //def addUser = Action(parse.json[User](userReadsTolerateEmptyName)) { implicit request =>
  def addUser = Action(parse.json[User]) { implicit request =>

      DAO.addUser(request.body) match {
        case Success(user) =>
          Ok(Json.toJson(user))
        case Failure(error) =>
          BadRequest(error.getMessage)
      }
  }

  def getUser(name : String) = Action {
    DAO.getUser(name) match {
      case Success(user) =>
        Ok(Json.toJson(user))
      case Failure(error) =>
        BadRequest(error.getMessage)
    }
  }

  def allNames = Action {
    val arrayOfNames = DAO.users.keySet.toArray
    Ok(Json.toJson(arrayOfNames))
  }

}
