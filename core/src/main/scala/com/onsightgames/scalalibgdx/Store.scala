package com.onsightgames.scalalibgdx

import com.onsightgames.scalalibgdx.events.Event

case class Store(private var _components : Set[Component[_ <: Entity]]) extends Logging {

  def components : Set[Component[_ <: Entity]] = _components

  def dispatch(event : Event) : Unit = {
    _components = _components.flatMap(_ update event)
  }

  def addComponent(component : Component[_ <: Entity]) : Unit = {
    info(s"Was ${_components.size} components")
    _components = _components + component
    info(s"Now ${_components.size} components")
  }

  def state : Iterable[Entity] = {
    _components.map(_.state)
  }
}