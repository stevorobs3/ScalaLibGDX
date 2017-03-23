package com.onsightgames.scalalibgdx.experiments

import java.util.UUID
import com.onsightgames.scalalibgdx.experiments.events.EventDetector

case class StoreData(
  reducers       : Map[UUID, Reducer[GameObject]],
  eventDetectors : Map[UUID, EventDetector],
  gameObjects    : Iterator[GameObject]
)

trait Store {
  var storeData : StoreData

  def processEvents() : Unit = {
    val events = storeData.eventDetectors.values.flatMap(_.generate)
    processEvents(events)
  }

  def processEvents(events : Any*) : Unit = {
    val gameObjects = storeData.gameObjects.map { gameObject =>
      events.foldLeft(gameObject) {
        case (previous, event) =>
          storeData.reducers(previous.id).reduce.lift((previous, event)).getOrElse(previous)
      }
    }
    storeData = storeData.copy(gameObjects = gameObjects)
  }

  // initial list of reducers

  //        root
  ///      /    \
  //     ship  alien fleet
  //           /     \
  //          boss   alien
  //          /
  //        minions


}

