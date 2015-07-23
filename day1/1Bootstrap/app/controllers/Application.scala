package controllers

import play.api._
import play.api.mvc._

class Application extends Controller {

  def sayHi = Action {
    Ok("Hi")
  }

  def echo(payload : String) = Action {
    Ok(payload)
  }

  //taken from https://www.playframework.com/documentation/2.4.x/ScalaBodyParsers

  def echoQueryString = Action { implicit request =>
    Ok(request.queryString.toString())
  }

  // Accept only 10 Bytes of data.
  def upTo10Bytes = Action(parse.text(maxLength = 10)) { request =>
    Ok("Got: " + request.body)
  }


}
