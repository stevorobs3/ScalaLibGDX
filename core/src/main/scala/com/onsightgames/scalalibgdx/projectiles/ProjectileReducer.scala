package com.onsightgames.scalalibgdx.projectiles

import com.onsightgames.scalalibgdx.events.Event
import com.onsightgames.scalalibgdx.events.LifecycleEventEmitter.Update
import com.onsightgames.scalalibgdx.{Logging, Reducer}

object ProjectileReducer extends Reducer[Projectile] with Logging {
  override def reduce: PartialFunction[(Projectile, Event), Projectile] = {
    case (projectile, Update(_)) =>
      info("reducing projectile")
      projectile
  }
}
