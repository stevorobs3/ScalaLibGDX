package com.onsightgames.scalalibgdx

import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.{Gdx, Screen}
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.onsightgames.scalalibgdx.events.{KeyboardEventEmitter, LifecycleEventEmitter}
import com.onsightgames.scalalibgdx.libgdx.{Rectangle, Vector2}

object SpaceInvaders {

  private val screen = Rectangle(
    position   = Vector2.Zero,
    dimensions = Vector2(
      x = Gdx.graphics.getWidth.toFloat,
      y = Gdx.graphics.getHeight.toFloat
    )
  )

  private val components : Set[Component[_ <: Entity]] = Level.first(screen).components

  private val store = new Store(components)

  private val lifecycleEventEmitter = new LifecycleEventEmitter(store.dispatch, render)
  private val keyboardEventEmitter  = new KeyboardEventEmitter(store.dispatch)

  private lazy val batch  = new SpriteBatch

  private def render() : Unit = {
    Gdx.gl.glClearColor(0,0,0,1)
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
    batch.begin()
    store.components.foreach(_ render batch)
    batch.end()
  }

  def start : Screen = {
    keyboardEventEmitter.start()
    lifecycleEventEmitter
  }

}
