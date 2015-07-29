import model.User
import model.persistance.AnormDAO
import org.openqa.selenium.WebDriver
import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._

import play.api.test._
import play.api.test.Helpers._
import play.api.libs.json._
import model.JsonParsers._

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */

@RunWith(classOf[JUnitRunner])
class ApplicationSpec extends Specification {

  def app: FakeApplication = FakeApplication(additionalConfiguration = H2TestUsersDB.settings)

  "Application" should {

    "send 404 on a bad request" in new AppWithH2UsersDB( app) {
      route(FakeRequest(GET, "/")) must beSome.which(status(_) == NOT_FOUND)
    }

    "get an empty response with json content type" in new AppWithH2UsersDB( app) {
      val users = route(FakeRequest(GET, "/users")).get

      status(users) must equalTo(OK)

      contentType(users) must beSome.which(_ == "application/json")

      val response = contentAsString(users)
      response must_== "{}"
    }

    "get a JSON Object with the darioalessandro user only" in new AppWithH2UsersDB( app) {
      val user = User("darioalessandro", "Dario", "Alessandro")
      AnormDAO.addUser(user)
      val users = route(FakeRequest(GET, "/users")).get

      status(users) must equalTo(OK)

      contentType(users) must beSome.which(_ == "application/json")

      val response = contentAsString(users)

      response must_== """{"darioalessandro":{"name":"darioalessandro","first":"Dario","last":"Alessandro"}}"""
    }

    "create a new user called Hao and get it through the get user by name svc" in new AppWithH2UsersDB(app) {
      val user = User("hao", "Hao", "Weng")

      val addUserReq = route(FakeRequest(POST, "/user").withHeaders(("Content-Type", "application/json")).withBody(Json.toJson(user))).get

      status(addUserReq) must equalTo(OK)

      contentType(addUserReq) must beSome.which(_ == "application/json")

      val response = contentAsString(addUserReq)

      response must_== """{"name":"hao","first":"Hao","last":"Weng"}"""

      val getUserReq = route(FakeRequest(GET, s"/user/${user.name}")).get

      status(getUserReq) must equalTo(OK)

      contentType(getUserReq) must beSome.which(_ == "application/json")

      val response2 = contentAsString(getUserReq)

      response2 must_== """{"name":"hao","first":"Hao","last":"Weng"}"""
    }

    "should throw an error if we try to insert 2 users with the same name " in new AppWithH2UsersDB(app) {
      val user = User("hao", "Hao", "Weng")

      val addUserReq = route(FakeRequest(POST, "/user").withHeaders(("Content-Type", "application/json")).withBody(Json.toJson(user))).get
      status(addUserReq) must equalTo(OK)
      contentType(addUserReq) must beSome.which(_ == "application/json")

      val addUserReq2 = route(FakeRequest(POST, "/user").withHeaders(("Content-Type", "application/json")).withBody(Json.toJson(user))).get
      status(addUserReq2) must equalTo(BAD_REQUEST)
      contentType(addUserReq2) must beSome.which(_ == "text/plain")
    }

    "should throw an error when trying to get user by name that does not exist" in new AppWithH2UsersDB(app) {
      val getUserReq = route(FakeRequest(GET, s"/user/anyuser")).get
      status(getUserReq) mustNotEqual OK
      status(getUserReq) must equalTo(BAD_REQUEST)
      contentType(getUserReq) must beSome.which(_ == "text/plain")
    }

    "Insert 100 users and return 100 users" in new AppWithH2UsersDB(app) {
      def generateUsers : Iterator[User] = {
        val users = for(i <- 1 to 100) yield {User(i.toString, i.toString, i.toString)}
        users.toIterator
      }

      val users = generateUsers

      while(users.hasNext) {
        val addUserReq = route(FakeRequest(POST, "/user").withHeaders(("Content-Type", "application/json"))
          .withBody(Json.toJson(users.next()))).get
          status(addUserReq) must equalTo(OK)
      }

      AnormDAO.users.size must_== 100

    }
  }
}




