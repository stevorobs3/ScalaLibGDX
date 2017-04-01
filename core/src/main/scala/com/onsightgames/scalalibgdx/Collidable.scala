package com.onsightgames.scalalibgdx

import com.onsightgames.scalalibgdx.libgdx.BasePolygon

object Collidable {

  sealed trait Layer
  object Layers {
    case object Alien extends Layer
    case object Ship extends Layer
  }
}

trait Collidable[Shape <: BasePolygon[Shape]] extends BoundedEntity[Shape] {
  val collisionLayer : Collidable.Layer
}
