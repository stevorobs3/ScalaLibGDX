package com.onsightgames.scalalibgdx.events

import com.onsightgames.scalalibgdx.Entity

trait SingleTargetedEvent extends Event {

  val entityId : Entity.Id

  def isTarget(entity : Entity) : Boolean = entity.id == entityId

}
