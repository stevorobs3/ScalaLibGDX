package com.onsightgames.scalalibgdx.ship

import com.onsightgames.scalalibgdx.Reducer._
import com.onsightgames.scalalibgdx.events.BoundaryCollisionDetector.{Boundary, BoundaryCrossed}
import com.onsightgames.scalalibgdx.events.KeyboardEventEmitter.{Key, KeyDownEvent, KeyUpEvent}
import com.onsightgames.scalalibgdx.events.LifecycleEventEmitter.Update
import com.onsightgames.scalalibgdx.libgdx.Vector2
import com.onsightgames.scalalibgdx.projectiles.Projectile
import com.onsightgames.scalalibgdx.{Component, Entity, Logging, Reducer}

object ShipReducer extends Reducer[Ship] with Logging {

  override def reduce: FullReducerFunc[Ship] = {
    handleUpdate orElse
      handleAcceleration orElse
      handleBoundaryCrossing andThen((_,Set.empty[Component[_ <: Entity]])) orElse
      handleFiring
  }

  private def handleUpdate : PartialReducerFunc[Ship] = {
    case (ship, _: Update) => ship.update()
  }

  private def handleAcceleration : PartialReducerFunc[Ship] = {
    case (ship, KeyDownEvent(Key.Left))  => ship.accelerate(Vector2(-1f,  0f))
    case (ship, KeyDownEvent(Key.Right)) => ship.accelerate(Vector2( 1f,  0f))
    case (ship, KeyDownEvent(Key.Down))  => ship.accelerate(Vector2( 0f, -1f))
    case (ship, KeyDownEvent(Key.Up))    => ship.accelerate(Vector2( 0f,  1f))

    case (ship, KeyUpEvent(Key.Left))    => ship.accelerate(Vector2( 1f,  0f))
    case (ship, KeyUpEvent(Key.Right))   => ship.accelerate(Vector2(-1f,  0f))
    case (ship, KeyUpEvent(Key.Down))    => ship.accelerate(Vector2( 0f,  1f))
    case (ship, KeyUpEvent(Key.Up))      => ship.accelerate(Vector2( 0f, -1f))
  }

  private def handleBoundaryCrossing : PartialReducerFunc[Ship] = {
    case (ship, boundaryCrossed : BoundaryCrossed) if boundaryCrossed.isTarget(ship) =>
      wrapScreen(ship, boundaryCrossed)
  }

  private def handleFiring : FullReducerFunc[Ship]= {
    case (ship : Ship, KeyDownEvent(Key.Space)) =>
      info("Handling Firing")

      Tuple2(ship, Set(Projectile.create(ship.nosePosition, ship.fireVelocity)))
  }

  private def wrapScreen(ship: Ship, boundaryCrossed: BoundaryCrossed) = {
    val position = ship.boundingBox.bottomLeft
    val screen   = boundaryCrossed.screen

    val newPosition = boundaryCrossed.boundary match {
      case Boundary.Left   => position.copy(x = position.x + screen.width)
      case Boundary.Right  => position.copy(x = position.x - screen.width)
      case Boundary.Bottom => position.copy(y = position.y + screen.height)
      case Boundary.Top    => position.copy(y = position.y - screen.height)
    }
    ship.copy(boundingBox = ship.boundingBox.copy(bottomLeft = newPosition))
  }
}