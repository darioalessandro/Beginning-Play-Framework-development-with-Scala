package controllers

import play.api.mvc._
import play.api.libs.json._
import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._


/**
 * Created by adminuser on 7/30/15.
 */

case class Item(name:String, description:String)

case class ItemReq(item:Item, quantity:Int)


class ShoppingCart extends Controller{

   var items = Map(Item("ps4", "ps4") -> 1, Item("xbox", "xbox") -> 2 )

  implicit val itemParser= Json.format[Item]
  implicit val itemReqParser= Json.format[ItemReq]

  def printableUsers : Map[String, Int] = {
    items.map{case(item, quantity) => item.name -> quantity}
  }



    def addItem = Action(parse.json[ItemReq]) { implicit request =>

      items =items + (request.body.item ->  request.body.quantity )
      Ok(Json.toJson(request.body))
    }

  def deleteItem = Action(parse.json[ItemReq]) {implicit request =>

      items = items - request.body.item

      Ok(Json.toJson(request.body))
  }

  def checkout = Action{

      Ok("checkout")
  }


  def listItems = Action{

    Ok(Json.toJson(printableUsers))
  }

}
