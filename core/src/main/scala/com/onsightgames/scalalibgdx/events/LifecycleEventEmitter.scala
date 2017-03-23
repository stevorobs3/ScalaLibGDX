package com.onsightgames.scalalibgdx.events

import com.badlogic.gdx.Screen
import com.onsightgames.scalalibgdx.HasLogger

object LifecycleEventEmitter {
  case class Update(timeElapsed : Float) extends Event
}

class LifecycleEventEmitter(update : LifecycleEventEmitter.Update => Unit, draw : () => Unit)
  extends Screen
  with HasLogger {

  override val LogId: String = "LifecycleEventEmitter"

  import LifecycleEventEmitter._

  override def hide(): Unit = {
    info("Hiding")
  }

  override def resize(width: Int, height: Int): Unit = {
    info(s"Resizing to $width*$height")
  }

  override def dispose(): Unit = {
    info("Disposing")
  }

  override def pause(): Unit = {
    info("Pausing")
  }

  override def show(): Unit = {
    info("Showing")
  }

  override def resume(): Unit = {
    info("Resuming")
  }

  override def render(delta: Float): Unit = {
    update(Update(delta))
    draw()
  }
}