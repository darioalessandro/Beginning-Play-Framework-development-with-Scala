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

import play.api.mvc._

class Routing extends Controller {

  def sayHi = Action {
    Ok("Hi").withHeaders("key" -> "value", "key2" -> "value", "key3" -> "value")
  }

  def echo(payload : String) = Action {
    Ok(payload)
  }

  //taken from https://www.playframework.com/documentation/2.4.x/ScalaBodyParsers

  def echoQueryString = Action { implicit request =>
    Ok(request.rawQueryString)
  }

  def echoQueryStringAsMap = Action { implicit  request =>
    Ok(request.queryString.toString)
  }

  // Accept only 10 Bytes of data.
  def upTo10Bytes = Action(parse.text(maxLength = 10)) { request =>
    Ok("Got: " + request.body)
  }


}
