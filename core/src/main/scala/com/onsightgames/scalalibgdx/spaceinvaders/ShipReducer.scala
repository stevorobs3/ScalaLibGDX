package com.onsightgames.scalalibgdx.spaceinvaders

import com.onsightgames.scalalibgdx.Ship
import com.onsightgames.scalalibgdx.experiments.Reducer
import com.onsightgames.scalalibgdx.experiments.events.KeyboardEvent

class ShipReducer extends Reducer[Ship] {
  override val initialState: Ship = Ship.atStartingPosition
  override def reduce: PartialFunction[(Ship, Any), Ship] = {
    case (ship, KeyboardEvent(_)) =>
      ship
  }
}