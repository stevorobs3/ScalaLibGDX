package com.onsightgames.scalalibgdx

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.onsightgames.scalalibgdx.events.Event

object Component {
  val EmptySet = Set.empty[Component[_ <: Entity]]
}

case class Component[State <: Entity](
  state   : State,
  reducer : Reducer[State],
  views   : Set[View[State]]
) {
  import scala.language.existentials

  lazy val entityId : Entity.Id = state.id

  def update(event : Event) : Set[Component[Entity]] = {
    val (newState, createdComponents) = reducer
      .reduce
      .applyOrElse((state, event), (_ : (State, Event)) => (state, Set.empty))
    createdComponents.asInstanceOf[Set[Component[Entity]]] + copy(state = newState)
  }

  def render(batch : SpriteBatch) : Unit = {
    views foreach (_.render(state, batch))
  }

}
