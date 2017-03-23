package com.onsightgames.scalalibgdx.experiments

import java.util.UUID

import com.onsightgames.scalalibgdx.experiments.events.CoreGameEvents.Update
import com.onsightgames.scalalibgdx.experiments.events.{CoreGameEvents, EventDetector}

case class StoreData(
  reducers       : Map[UUID, Reducer[GameObject]],
  renderers      : Seq[Renderer[GameObject]],
  eventDetectors : Map[UUID, EventDetector],
  gameObjects    : Seq[GameObject]
)

trait Store {
  val coreGameEvents = CoreGameEvents(this)
  var storeData: StoreData = StoreData(
    Map.empty,
    Seq.empty,
    Map((coreGameEvents.id, coreGameEvents)),
    Seq.empty
  )

  def processUpdate(update : Update) : Unit = {
    val events = storeData.eventDetectors.values.flatMap(_.generate).toSeq
    processEvents(events :+ update)
    processRendering()
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

  private def processRendering() = {

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

