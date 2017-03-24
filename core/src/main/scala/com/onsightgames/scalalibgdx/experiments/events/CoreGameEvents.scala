package com.onsightgames.scalalibgdx.experiments.events

import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.{Gdx, Screen}
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.onsightgames.scalalibgdx.experiments.Store

object CoreGameEvents {
  def apply(store : Store) : CoreGameEvents = new CoreGameEvents(store)

  sealed trait Event
  case object Hide                              extends Event
  case class  Resize(width : Int, height : Int) extends Event
  case class  Update(delta : Float)             extends Event
  case object Dispose                           extends Event
  case object Pause                             extends Event
  case object Show                              extends Event
  case object Resume                            extends Event

}

class CoreGameEvents(store : Store) extends Screen with EventDetector {
  import CoreGameEvents._
  override def hide()                          : Unit = store processEvents Hide
  override def resize(width: Int, height: Int) : Unit = store processEvents Resize(width, height)
  override def dispose()                       : Unit = store processEvents Dispose
  override def pause()                         : Unit = store processEvents Pause
  override def show()                          : Unit = store processEvents Show
  override def resume()                        : Unit = store processEvents Resume

  override def render(delta: Float)            : Unit = {
    Gdx.gl.glClearColor(0,0,0,1)
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
    batch.begin()
    store processUpdate Update(delta)
    store processRendering batch
    batch.end()
  }

  private lazy val batch  = new SpriteBatch
  override def generate: List[Any] = {
    List.empty
  }
}
