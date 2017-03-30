package com.onsightgames.scalalibgdx.events

import com.badlogic.gdx.Screen
import com.onsightgames.scalalibgdx.Logging

object LifecycleEventEmitter {
  case class  Update(timeElapsed : Float)       extends Event
  case class  Resize(width : Int, height : Int) extends Event
  case object Hide                              extends Event
  case object Dispose                           extends Event
  case object Pause                             extends Event
  case object Show                              extends Event
  case object Resume                            extends Event
}

class LifecycleEventEmitter(
  update   : LifecycleEventEmitter.Update => Unit,
  draw     : ()    => Unit,
  dispatch : Event => Unit
)
  extends Screen
  with Logging {

  import LifecycleEventEmitter._

  override def hide(): Unit = {
    info("Hiding")
    dispatch(Hide)
  }

  override def resize(width: Int, height: Int): Unit = {
    info(s"Resizing to $width*$height")
    dispatch(Resize(width, height))
  }

  override def dispose(): Unit = {
    info("Disposing")
    dispatch(Dispose)
  }

  override def pause(): Unit = {
    info("Pausing")
    dispatch(Pause)
  }

  override def show(): Unit = {
    info("Showing")
    dispatch(Show)
  }

  override def resume(): Unit = {
    info("Resuming")
    dispatch(Resume)
  }

  override def render(delta: Float): Unit = {
    update(Update(delta))
    draw()
  }
}