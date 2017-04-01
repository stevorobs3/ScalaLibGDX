package com.onsightgames.scalalibgdx

import com.onsightgames.scalalibgdx.events.Event

class Store(var components : Set[Component[_ <: Entity]]) {

  def dispatch(event : Event) : Unit = {
    components = components.map(_ update event)
  }

  def getState : Iterable[Entity] = {
    components.flatMap(_.getAllEntites)
  }

}
