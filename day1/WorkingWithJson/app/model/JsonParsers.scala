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

import play.api.libs.json._
import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._

object JsonParsers {

  implicit val userFormatter = Json.format[User]


  val userWriterWithNoName = new Writes[User] {
    def writes(c: User): JsValue = {
      Json.obj(
        "first" -> c.first,
        "last" -> c.last
      )
    }
  }

  val userReadsTolerateEmptyName = new Reads[User] {
    def reads(o:JsValue) : JsResult[User] = {
      o match {
        case obj : JsObject =>
          val first = (o \ "first").asOpt[String]
          val last = (o \ "last").asOpt[String]
          val name = (o \ "name").asOpt[String]

          (name, first, last) match {
            case (_, None, _) =>
              JsError("first name should be defined")

            case (_, _, None) =>
              JsError("last name should be defined")

            case ( None, Some(f), Some(l)) =>
              JsSuccess( User(f + l, f, l))

            case (Some(u), Some(f), Some(l)) =>
              JsSuccess(User(u,f,l))
          }

        case other =>
          JsError(s"json should be an object but it is an ${other.getClass}")
      }

    }
  }


}
