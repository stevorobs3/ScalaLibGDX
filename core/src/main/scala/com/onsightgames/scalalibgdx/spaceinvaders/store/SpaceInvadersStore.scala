package com.onsightgames.scalalibgdx.spaceinvaders.store

import com.onsightgames.scalalibgdx.experiments.Store
import com.onsightgames.scalalibgdx.experiments.events.CoreGameEvents.Update
import com.onsightgames.scalalibgdx.experiments.events.EventDetector
import com.onsightgames.scalalibgdx.spaceinvaders.objects.Level
import com.onsightgames.scalalibgdx.spaceinvaders.reducers.AlienFleetReducer
import com.onsightgames.scalalibgdx.spaceinvaders.renderers.AlienFleetRenderer

object SpaceInvadersStore extends Store {
  val LogId = "SpaceInvadersStore"
  storeData = {
    val alienFleet = Level.One.alienFleet

    info(s"${AlienFleetReducer.reduce.isDefinedAt((alienFleet, Update(0.1f)))}")
    info(s"${AlienFleetReducer.create.reduce.isDefinedAt((alienFleet, Update(0.1f).asInstanceOf[Any]))}")
    val reducers       = Map((alienFleet.id, AlienFleetReducer.create))
    val renderers      = Map((alienFleet.id, AlienFleetRenderer.create))
    val eventDetectors = Seq.empty[EventDetector]
    val gameObjects    = Map((alienFleet.id, alienFleet))

    storeData.copy(
      reducers,
      renderers,
      storeData.eventDetectors ++ eventDetectors,
      gameObjects
    )
  }
}