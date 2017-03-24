package com.onsightgames.scalalibgdx.spaceinvaders.reducers

import com.onsightgames.scalalibgdx.Ship
import com.onsightgames.scalalibgdx.experiments.{GameObject, Reducer}
import com.onsightgames.scalalibgdx.experiments.events.KeyboardEvent

class ShipReducer extends Reducer[Ship] {
  val LogId = "ShipReducer"
  override def reduce: PartialFunction[(GameObject, Any), GameObject] = {
    case (ship : Ship, KeyboardEvent(_)) =>
      ship
  }
}