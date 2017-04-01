package com.onsightgames.scalalibgdx.events

import com.onsightgames.scalalibgdx.Entity

trait MutliTargetedEvent extends Event {

  val entityIds : Set[Entity.Id]

  def isTarget(entity : Entity) : Boolean = entityIds contains entity.id

}
