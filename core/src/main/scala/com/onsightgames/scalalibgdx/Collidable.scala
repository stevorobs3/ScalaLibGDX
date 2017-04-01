package com.onsightgames.scalalibgdx

object Collidable {

  sealed trait Layer
  object Layers {
    case object Alien extends Layer
    case object Ship extends Layer
  }
}

trait Collidable extends BoundedEntity {
  val collisionLayer : Collidable.Layer
}
