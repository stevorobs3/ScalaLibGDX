package com.onsightgames.scalalibgdx

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.{GL20, OrthographicCamera}
import com.badlogic.gdx.{Gdx, Screen => LScreen}
import com.onsightgames.scalalibgdx.events.LifecycleEventEmitter.Update
import com.onsightgames.scalalibgdx.events._

object SpaceInvaders {
  val ScreenWidth = 500
  val ScreenHeight = 900

  val screen : Screen = Screen(ScreenWidth, ScreenHeight)

  private val store = Store(Set.empty)
  Level.first(screen.rectangle).components.foreach(store.addComponent)


  private val lifecycleEventEmitter     = new LifecycleEventEmitter(update, render, store.dispatch)
  private val keyboardEventEmitter      = new KeyboardEventEmitter(store.dispatch)
  private val boundaryCollisionDetector = new BoundaryCollisionDetector

  private lazy val batch  = new SpriteBatch
  private lazy val camera = new OrthographicCamera()
  camera.setToOrtho(false, screen.width.toFloat, screen.height.toFloat)

  private def render() : Unit = {
    Gdx.gl.glClearColor(0,0,0,1)
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
    camera.update()
    batch.setProjectionMatrix(camera.combined)
    batch.begin()
    store.components.foreach(_ render batch)
    batch.end()
  }

  def update(updateEvent : Update) : Unit = {
    store.dispatch(updateEvent)
    boundaryCollisionDetector
      .run(screen.rectangle, store.state)
      .foreach(store.dispatch)
  }

  def start : LScreen = {
    keyboardEventEmitter.start()
    lifecycleEventEmitter
  }
}