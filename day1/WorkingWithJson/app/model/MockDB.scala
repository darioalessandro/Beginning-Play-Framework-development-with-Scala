package model

/*
Copyright [2015] [Dario A Lencina Talarico]

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0
Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations
 under the License.
 */

import scala.util.{Failure, Try, Success}

object MockDB {

  private var _users = List(
    User("tania", "Tania", "Curiel"),
    User("joe", "Joseph", "Guido"),
    User("aarthimai", "Aarthimai", "Krishnamoorthy"),
    User("alex", "Alex", "Vandyk")
  )

  def users : List[User] = _users

  def addUser(user : User) : Try[User] = {
      val currentUsers = users
      if(currentUsers.exists(u => u.name == user.name)){
        Failure(new Throwable(s"user ${user.name} already exists"))
      } else {
        _users = user +: _users
        Success(user)
      }
  }

  def getUser(name : String) : Try[User] = {
    val user = users.find(user => user.name == name)

    user match {
      case Some(u) =>
        Success(u)

      case None =>
        Failure(new Throwable(s"Unable to find user with name $name"))

    }

  }

}
