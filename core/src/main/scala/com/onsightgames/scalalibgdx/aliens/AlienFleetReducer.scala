package com.onsightgames.scalalibgdx.aliens

import com.onsightgames.scalalibgdx.Reducer
import com.onsightgames.scalalibgdx.events.BoundaryCollisionDetector.{Boundary, BoundaryTouched}
import com.onsightgames.scalalibgdx.events.Event
import com.onsightgames.scalalibgdx.events.LifecycleEventEmitter.Update

object AlienFleetReducer extends Reducer[AlienFleet] {

  override def reduce: PartialFunction[(AlienFleet, Event), AlienFleet] = {
    case (fleet, Update(timeElapsed)) => fleet.update(timeElapsed)

    case (fleet, boundaryTouched : BoundaryTouched) if shouldChangeXSpeed(boundaryTouched, fleet) =>
      val velocity = fleet.velocity
      fleet.copy(velocity = velocity.copy(x = -velocity.x))
  }

  private def shouldChangeXSpeed(boundaryTouched: BoundaryTouched, fleet: AlienFleet) : Boolean = {
    boundaryTouched.isTarget(fleet) && (boundaryTouched.boundary match {
      case Boundary.Left | Boundary.Right => true
      case _                              => false
    })
  }
}
