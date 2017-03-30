package com.onsightgames.scalalibgdx

import java.util.UUID

object Entity {
  type Id = UUID
  def newId : Id = UUID.randomUUID()
}

trait Entity {
  val id : Entity.Id
}