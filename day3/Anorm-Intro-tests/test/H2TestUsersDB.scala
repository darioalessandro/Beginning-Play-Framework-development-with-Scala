import anorm._
import play.api.Play.current
import play.api.db.DB

/**
 * Created by adminuser on 3/12/15.
 */
object H2TestUsersDB {

  def createSchema() : Boolean = {
    DB.withConnection("users") { implicit c =>
      SQL(
        s"""
         CREATE  TABLE IF NOT EXISTS users (
           |  `name` VARCHAR(45) NOT NULL ,
           |  `first` VARCHAR(45) NOT NULL ,
           |  `last` VARCHAR(45) NOT NULL ,
           |  PRIMARY KEY (`name`) ,
           |  UNIQUE INDEX `name_UNIQUE` (`name` ASC) );
        """.replace("|","")
      ).execute()
    }
  }

  val settings = Map(
    "db.users.driver"->"org.h2.Driver",
    "db.users.url" -> "jdbc:h2:mem:users;MODE=MYSQL",
    "db.users.user" -> "sa",
    "db.users.password" -> "")
}
