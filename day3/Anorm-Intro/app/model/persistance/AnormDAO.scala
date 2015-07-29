package model.persistance

import anorm._
import model.User
import play.api.db.DB
import play.api.Play.current
import scala.util.{Success, Failure, Try}
import anorm.SqlParser.str
/*
Copyright [2015] [Dario A Lencina Talarico]

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0
Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations
 under the License.
 */

object AnormDAO extends DAO {

  def users : Map[String, User] = DB.withConnection("users") { implicit c =>
    SQL(
      """ select * from users"""
    ).on()().collect {
      case userRow =>
        val name = userRow[String]("name")
        val first = userRow[String]("first")
        val last = userRow[String]("last")
        name -> User(name, first, last)
    }.toMap
  }

  def addUser(user : User) : Try[User] = {
    val success = !DB.withConnection("users") { implicit connection =>
      SQL(
        """insert into users set name = {name}, first = {first}, last = {last} """
      ).on(
          'name -> user.name,
          'first -> user.first,
          'last -> user.last
        ).execute()
    }
    if(success)
      Success(user) //withResult
    else
      Failure(new Throwable(s"unable to insert user $user"))
  }

  def getUser(name : String) : Try[User] = {
    val user = DB.withConnection("users") { implicit c =>
      SQL(
        """ select * from users where name={name} """
      ).on('name -> name)().collect {
        case userRow =>
          val name = userRow[String]("name")
          val first = userRow[String]("first")
          val last = userRow[String]("last")
          User(name, first, last)
      }.headOption
    }

    user match {
      case Some(u) =>
        Success(u)

      case None =>
        Failure(new Throwable(s"Unable to find user with name $name"))
    }
  }
}
