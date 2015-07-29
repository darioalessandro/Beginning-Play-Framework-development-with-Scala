import model.User

val number : Option[User]= Some(User("name", "first", "last"))

number match {

  case None =>
    println("None")

  case _ =>
    println("wildcard : "+ number)
}