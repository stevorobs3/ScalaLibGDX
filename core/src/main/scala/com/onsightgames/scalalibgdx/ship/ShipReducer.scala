package com.onsightgames.scalalibgdx.ship

import com.onsightgames.scalalibgdx.Reducer
import com.onsightgames.scalalibgdx.events.Event
import com.onsightgames.scalalibgdx.events.LifecycleEventEmitter.Update

object ShipReducer extends Reducer[Ship] {

  override def reduce: PartialFunction[(Ship, Event), Ship] = {
    case (ship, update: Update) => ship.update()
  }
}
