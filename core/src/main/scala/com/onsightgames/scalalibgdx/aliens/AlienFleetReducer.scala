package com.onsightgames.scalalibgdx.aliens

import com.onsightgames.scalalibgdx.Reducer
import com.onsightgames.scalalibgdx.events.Event
import com.onsightgames.scalalibgdx.events.LifecycleEventEmitter.Update

object AlienFleetReducer extends Reducer[AlienFleet] {

  override def reduce: PartialFunction[(AlienFleet, Event), AlienFleet] = {
    case (alienFleet, Update(timeElapsed)) => alienFleet.update(timeElapsed)
  }
}
