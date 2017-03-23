package com.onsightgames.scalalibgdx.experiments

import java.util.UUID
import com.onsightgames.scalalibgdx.experiments.events.EventDetector

trait Store {
  val objects        : Iterator[GameObject]
  val reducers       : Map[UUID, Reducer[GameObject]]
  val eventDetectors : List[EventDetector]
  var gameObjects    : Iterator[GameObject]

  def processEvents() : Unit = {
    val events = eventDetectors.flatMap(_.generate)
    gameObjects = gameObjects.map{gameObject =>
      events.foldLeft(gameObject){
        case (previous, event) =>
          reducers(previous.id).reduce.lift((previous, event)).getOrElse(previous)
      }
    }
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