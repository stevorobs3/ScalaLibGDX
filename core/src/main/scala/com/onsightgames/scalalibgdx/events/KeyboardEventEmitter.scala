package com.onsightgames.scalalibgdx.events

import com.badlogic.gdx.Input.Keys
import com.badlogic.gdx.{Gdx, InputAdapter}

object KeyboardEventEmitter {

  sealed trait Key
  object Key {
    case object Left  extends Key
    case object Right extends Key
    case object Down  extends Key
    case object Up    extends Key

  }

  case class KeyUpEvent(key : Key)   extends Event
  case class KeyDownEvent(key : Key) extends Event

  private val GdxKeyMapping = Map(
    Keys.LEFT  -> Key.Left,
    Keys.RIGHT -> Key.Right,
    Keys.DOWN  -> Key.Down,
    Keys.UP    -> Key.Up
  )
}

class KeyboardEventEmitter(emit : Event => Unit) {

  import KeyboardEventEmitter._

  private lazy val keyboardAdapter = new InputAdapter() {

    override def keyDown(keycode : Int) : Boolean = {
      emitIfRegistered(KeyDownEvent, keycode)
    }

    override def keyUp(keycode : Int) : Boolean = {
      emitIfRegistered(KeyUpEvent, keycode)
    }

    private def emitIfRegistered(buildEvent : Key => Event, keycode : Int) : Boolean = {
      val event = GdxKeyMapping.get(keycode).map(buildEvent)
      event.foreach(emit)
      event.isDefined
    }
  }

  def start() : Unit = {
    Gdx.input.setInputProcessor(keyboardAdapter)
  }

}
