package com.onsightgames.scalalibgdx.events

import com.onsightgames.scalalibgdx.{Entity, BoundedEntity}
import com.onsightgames.scalalibgdx.libgdx.Rectangle

object BoundaryCollisionDetector {

  sealed trait Boundary
  object Boundary {
    case object Left   extends Boundary
    case object Right  extends Boundary
    case object Top    extends Boundary
    case object Bottom extends Boundary
  }

  case class BoundaryTouched(
    entityId : Entity.Id,
    boundary : Boundary,
    screen   : Rectangle
  ) extends TargetedEvent

  case class BoundaryCrossed(
    entityId : Entity.Id,
    boundary : Boundary,
    screen   : Rectangle
  ) extends TargetedEvent
}

class BoundaryCollisionDetector {

  import BoundaryCollisionDetector._

  def run(screen : Rectangle, entities : Iterable[Entity]) : Iterable[Event] = {
    entities.foldLeft(Set.empty[Event]) {
      case (events, entity : BoundedEntity) => events ++ detectBoundaryEvents(screen, entity)
      case (events, _)                      => events
    }
  }

  private def detectBoundaryEvents(screen : Rectangle, entity : BoundedEntity) : Option[Event] = {
    val rectangle = entity.boundingBox
    if (screen contains rectangle)
      None
    else if (screen overlaps rectangle)
      Some(BoundaryTouched(entity.id, detectBoundaryTouched(screen, rectangle), screen))
    else
      Some(BoundaryCrossed(entity.id, detectBoundaryCrossed(screen, rectangle), screen))
  }

  private def detectBoundaryTouched(screen: Rectangle, rectangle: Rectangle) = {
    if      (screen.leftEdge   > rectangle.leftEdge)   Boundary.Left
    else if (screen.rightEdge  < rectangle.rightEdge)  Boundary.Right
    else if (screen.bottomEdge > rectangle.bottomEdge) Boundary.Bottom
    else                                               Boundary.Top
  }

  private def detectBoundaryCrossed(screen: Rectangle, rectangle: Rectangle) = {
    if      (screen.leftEdge   > rectangle.rightEdge) Boundary.Left
    else if (screen.rightEdge  < rectangle.leftEdge)  Boundary.Right
    else if (screen.bottomEdge > rectangle.topEdge)   Boundary.Bottom
    else                                              Boundary.Top
  }
}