package controllers

import play.api.mvc._


/**
 * Created by adminuser on 7/30/15.
 */
class Inventory extends Controller{

  def inspectItem = Action{

    Ok("inspectItems")
  }

  def searchItems(item: String) = Action{

    Ok("searchItems")
  }


}
