package model

import scala.util.{Failure, Try, Success}

/**
 * Created by adminuser on 7/27/15.
 */
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

}
