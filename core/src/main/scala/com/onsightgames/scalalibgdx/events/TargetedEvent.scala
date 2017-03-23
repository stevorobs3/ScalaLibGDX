package com.onsightgames.scalalibgdx.events

import com.onsightgames.scalalibgdx.Entity

trait TargetedEvent extends Event {

  val entityId : Entity.Id

  def isTarget(entity : Entity) : Boolean = entity.id == entityId

}
