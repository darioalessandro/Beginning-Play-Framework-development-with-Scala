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

    "send 404 on a bad request" in new WithApplication() {
      route(FakeRequest(GET, "/")) must beSome.which(status(_) == NOT_FOUND)
    }

    "get an empty response with json content type" in new AppWithH2UsersDB(app = app) {
      val users = route(FakeRequest(GET, "/users")).get

      status(users) must equalTo(OK)

      contentType(users) must beSome.which(_ == "application/json")

      val response = contentAsString(users)
      response must_== "{}"
    }

    "get a JSON Object with the darioalessandro user only" in new AppWithH2UsersDB(app = app) {
      val user = User("darioalessandro", "Dario", "Alessandro")
      AnormDAO.addUser(user)
      val users = route(FakeRequest(GET, "/users")).get

      status(users) must equalTo(OK)

      contentType(users) must beSome.which(_ == "application/json")

      val response = contentAsString(users)

      response must_== """{"darioalessandro":{"name":"darioalessandro","first":"Dario","last":"Alessandro"}}"""
    }

    "create a new user called Hao and get it through the get user by name svc" in new AppWithH2UsersDB(app = app) {
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
  }
}




