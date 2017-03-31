package com.onsightgames.scalalibgdx.projectiles

import com.onsightgames.scalalibgdx.{Component, Entity}
import com.onsightgames.scalalibgdx.libgdx.Vector2

object Projectile {
  def create(
    initialPosition : Vector2,
    velocity        : Vector2
  ) : Component[Projectile] = Component(
    Projectile(initialPosition, velocity),
    ProjectileReducer,
    Set(ProjectileView)
  )
}

case class Projectile(
  position : Vector2,
  velocity : Vector2,
  id       : Entity.Id = Entity.newId

) extends Entity {
  lazy val x : Float = position.x
  lazy val y : Float = position.y
}