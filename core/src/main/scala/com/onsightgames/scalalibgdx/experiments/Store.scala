package com.onsightgames.scalalibgdx.experiments

import java.util.UUID

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.onsightgames.scalalibgdx.Logging
import com.onsightgames.scalalibgdx.experiments.events.CoreGameEvents.Update
import com.onsightgames.scalalibgdx.experiments.events.{CoreGameEvents, EventDetector}

case class StoreData(
  reducers       : Map[UUID, Reducer[GameObject]],
  renderers      : Map[UUID, Renderer[GameObject]],
  eventDetectors : Seq[EventDetector],
  gameObjects    : Map[UUID,GameObject]
)

trait Store extends Logging{
  val coreGameEvents = CoreGameEvents(this)
  var storeData : StoreData = StoreData(
    Map.empty,
    Map.empty,
    Seq(coreGameEvents),
    Map.empty
  )

  def processUpdate(update : Update) : Unit = {
    val events = storeData.eventDetectors.flatMap(_.generate)
    processEvents(events :+ update : _*)
  }

  def processRendering(batch : SpriteBatch) : Unit = {
    storeData.renderers.foreach{
      case (gameObjectId, renderer) =>
        renderer.render(storeData.gameObjects(gameObjectId), batch)
    }
  }

  def processEvents(events : Any*) : Unit = {
    val gameObjects = storeData.gameObjects.mapValues { gameObject =>
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

