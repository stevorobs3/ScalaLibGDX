package com.onsightgames.scalalibgdx.aliens

import com.onsightgames.scalalibgdx.Reducer
import com.onsightgames.scalalibgdx.events.BoundaryCollisionDetector.{Boundary, BoundaryTouched}
import com.onsightgames.scalalibgdx.events.CollisionDetector.Collision
import com.onsightgames.scalalibgdx.events.Event
import com.onsightgames.scalalibgdx.events.LifecycleManager.Update

object AlienFleetReducer extends Reducer[AlienFleet] {

  override def reduce: PartialFunction[(AlienFleet, Event), AlienFleet] = {
    case (fleet, Update(timeElapsed)) => fleet.update(timeElapsed)

    case (fleet, collision : Collision) =>
      val aliens = fleet.aliens map { _ filter(alien => !collision.isTarget(alien)) }
      fleet.copy(aliens = aliens)

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
