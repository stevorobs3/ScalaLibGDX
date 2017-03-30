package com.onsightgames.scalalibgdx

import com.onsightgames.scalalibgdx.events.Event

case class Store(private var _components : Set[Component[_ <: Entity]]) {

  def components : Set[Component[_ <: Entity]] = _components

  def dispatch(event : Event) : Unit = {
    _components = _components.map(_ update event)
  }

  def getState : Iterable[Entity] = {
    _components.map(_.state)
  }

}