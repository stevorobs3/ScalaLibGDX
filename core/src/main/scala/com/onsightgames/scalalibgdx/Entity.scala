package com.onsightgames.scalalibgdx

import java.util.UUID

object Entity {
  type Id = UUID
}

trait Entity {

  val id : Entity.Id = UUID.randomUUID()

}
