package model

import play.api.libs.json._
import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._

/**
 * Created by adminuser on 7/27/15.
 */
object JsonParsers {
  implicit val userFormatter = Json.format[User]
}
