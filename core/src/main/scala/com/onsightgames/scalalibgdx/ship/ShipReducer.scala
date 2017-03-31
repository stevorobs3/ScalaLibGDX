package com.onsightgames.scalalibgdx.ship

import com.onsightgames.scalalibgdx.Reducer
import com.onsightgames.scalalibgdx.events.BoundaryCollisionDetector.{Boundary, BoundaryCrossed}
import com.onsightgames.scalalibgdx.events.Event
import com.onsightgames.scalalibgdx.events.KeyboardEventEmitter.{Key, KeyDownEvent, KeyUpEvent}
import com.onsightgames.scalalibgdx.events.LifecycleEventEmitter.Update
import com.onsightgames.scalalibgdx.libgdx.Vector2

object ShipReducer extends Reducer[Ship] {
  
  val Acceleration = 15f

  override def reduce: PartialFunction[(Ship, Event), Ship] = {
    case (ship, Update(timeElapsed))               => ship.update(timeElapsed)

    case (ship, KeyDownEvent(Key.Left))  => ship.withAcceleration(Vector2(-Acceleration, 0))
    case (ship, KeyDownEvent(Key.Right)) => ship.withAcceleration(Vector2(+Acceleration, 0))
    case (ship, KeyDownEvent(Key.Down))  => ship.withAcceleration(Vector2(0, -Acceleration))
    case (ship, KeyDownEvent(Key.Up))    => ship.withAcceleration(Vector2(0, +Acceleration))

    case (ship, KeyUpEvent(Key.Left))    => ship.withAcceleration(Vector2(+Acceleration, 0))
    case (ship, KeyUpEvent(Key.Right))   => ship.withAcceleration(Vector2(-Acceleration, 0))
    case (ship, KeyUpEvent(Key.Down))    => ship.withAcceleration(Vector2(0, +Acceleration))
    case (ship, KeyUpEvent(Key.Up))      => ship.withAcceleration(Vector2(0, -Acceleration))

    case (ship, boundaryCrossed : BoundaryCrossed) if boundaryCrossed.isTarget(ship) =>
      wrapScreen(ship, boundaryCrossed)
  }

  private def wrapScreen(ship: Ship, boundaryCrossed: BoundaryCrossed) = {
    val screen = boundaryCrossed.screen

    val positionChange = boundaryCrossed.boundary match {
      case Boundary.Left   => Vector2(x = +screen.width, y = 0)
      case Boundary.Right  => Vector2(x = -screen.width, y = 0)
      case Boundary.Bottom => Vector2(x = 0, y = +screen.height)
      case Boundary.Top    => Vector2(x = 0, y = -screen.height)
    }
    ship.copy(boundingBox = ship.boundingBox.translate(positionChange))
  }
}
