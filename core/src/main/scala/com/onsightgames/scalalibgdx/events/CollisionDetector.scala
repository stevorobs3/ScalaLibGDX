package com.onsightgames.scalalibgdx.events

import com.onsightgames.scalalibgdx.events.CollisionDetector._
import com.onsightgames.scalalibgdx.{Collidable, Entity}

object CollisionDetector {

  type GroupedCollidables = Map[Collidable.Layer, Set[Collidable]]
  type CollisionMap = Map[Collidable.Layer, Collidable.Layer]

  val DefaultCollisionMap : CollisionMap = Map(Collidable.Layers.Ship -> Collidable.Layers.Alien)

  case class Collision(entityIds : Set[Entity.Id]) extends MutliTargetedEvent
}

class CollisionDetector(collisionMap : CollisionMap = DefaultCollisionMap) {

  def run(entities : Iterable[Entity]) : Iterable[Event] = {
    val collidables = extractCollidables(entities)
    detectCollisions(collidables)
  }

  private def extractCollidables(entities : Iterable[Entity]) : GroupedCollidables = {
    entities.foldLeft(Map() : GroupedCollidables) { case (collidables, entity) =>
      entity match {
        case collidable : Collidable => add(collidables, collidable)
        case _                       => collidables
      }
    }
  }

  private def add(collidables : GroupedCollidables, collidable: Collidable) = {
    val group = collidables.getOrElse(collidable.collisionLayer, Set())
    collidables + (collidable.collisionLayer -> (group + collidable))
  }

  private def detectCollisions(collidables : GroupedCollidables) = {
    collisionMap.foldLeft(Nil : List[Event]) { case (events, (layer, collidesWith)) =>
      val layerCollidables = collidables.getOrElse(layer, Set())
      val otherCollidables = collidables.getOrElse(collidesWith, Set())
      events ++ detectCollisionsBetweenLayers(layerCollidables, otherCollidables)
    }
  }

  private def detectCollisionsBetweenLayers(layer : Set[Collidable], other : Set[Collidable]) = {
    layer flatMap { collidable =>
      other flatMap { otherCollidable =>
        if (areColliding(collidable, otherCollidable)) {
          Some(Collision(Set(collidable.id, otherCollidable.id)))
        }
        else {
          None
        }
      }
    }
  }

  private def areColliding(collidable : Collidable, otherCollidable : Collidable) : Boolean = {
    collidable.id != otherCollidable.id &&
      collidable.boundingBox.overlaps(otherCollidable.boundingBox)
  }
}
