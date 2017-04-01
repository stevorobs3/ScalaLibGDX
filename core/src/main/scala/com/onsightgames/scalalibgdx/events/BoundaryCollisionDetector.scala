package com.onsightgames.scalalibgdx.events

import com.onsightgames.scalalibgdx.{BoundedEntity, Entity}
import com.onsightgames.scalalibgdx.libgdx.Rectangle
import com.badlogic.gdx.math.{Rectangle => LRectangle}

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
  ) extends SingleTargetedEvent

  case class BoundaryCrossed(
    entityId : Entity.Id,
    boundary : Boundary,
    screen   : Rectangle
  ) extends SingleTargetedEvent
}

class BoundaryCollisionDetector {

  import BoundaryCollisionDetector._

  def run(screen : Rectangle, entities : Iterable[Entity]) : Iterable[Event] = {
    entities.foldLeft(Set[Event]()) {
      case (events, entity : BoundedEntity[_]) => events ++ detectBoundaryEvents(screen, entity)
      case (events, _)                         => events
    }
  }

  private def detectBoundaryEvents(
    screen : Rectangle,
    entity : BoundedEntity[_]
  ) : Option[Event] = {
    val rectangle = entity.bounds.getBoundingRectangle
    if (screen contains rectangle)
      None
    else if (screen overlaps rectangle)
      Some(BoundaryTouched(entity.id, detectBoundaryTouched(screen, rectangle), screen))
    else
      Some(BoundaryCrossed(entity.id, detectBoundaryCrossed(screen, rectangle), screen))
  }

  private def detectBoundaryTouched(screen: Rectangle, rectangle: LRectangle) = {
    if (screen.leftEdge > rectangle.x)                         Boundary.Left
    else if (screen.rightEdge < rectangle.x + rectangle.width) Boundary.Right
    else if (screen.bottomEdge > rectangle.y)                  Boundary.Bottom
    else                                                       Boundary.Top
  }

  private def detectBoundaryCrossed(screen: Rectangle, rectangle: LRectangle) = {
    if (screen.leftEdge > rectangle.x + rectangle.width)         Boundary.Left
    else if (screen.rightEdge < rectangle.x)                     Boundary.Right
    else if (screen.bottomEdge > rectangle.y + rectangle.height) Boundary.Bottom
    else                                                         Boundary.Top
  }
}
