package com.onsightgames.scalalibgdx

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.onsightgames.scalalibgdx.events.Event

case class Component[State <: Entity](
  state         : State,
  reducer       : Reducer[State],
  views         : Set[View[State]],
  mapToEntities : State => Iterable[Entity] = (state: State) => Set(state)
) {

  lazy val entityId : Entity.Id = state.id

  def update(event : Event) : Component[State] = {
    val newState = reducer.reduce.applyOrElse((state, event), (_ : (State, Event)) => state)
    copy(state = newState)
  }

  def render(batch : SpriteBatch) : Unit = {
    views foreach (_.render(state, batch))
  }

  def getAllEntites : Iterable[Entity] = {
    mapToEntities(state)
  }

}
