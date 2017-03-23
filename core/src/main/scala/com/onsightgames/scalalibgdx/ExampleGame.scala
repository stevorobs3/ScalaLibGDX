package com.onsightgames.scalalibgdx

import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.{Gdx, Screen}
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.onsightgames.scalalibgdx.events.LifecycleEventEmitter
import com.onsightgames.scalalibgdx.libgdx.Vector2
import com.onsightgames.scalalibgdx.ship.{Ship, ShipReducer, ShipView}

object ExampleGame {

  private val components : Set[Component[_ <: Entity]] = Set(
    Component[Ship](
      state = Ship(
        dimensions   = Vector2(40f, 40f),
        position     = Vector2(Gdx.graphics.getWidth / 2f, Gdx.graphics.getHeight / 10f),
        velocity     = Vector2.Zero,
        acceleration = Vector2.Zero
      ),
      reducer = ShipReducer,
      views   = Set(ShipView)
    )
  )

  private val store = new Store(components)

  private val lifecycleEventEmitter = new LifecycleEventEmitter(store.dispatch, render)

  private lazy val batch  = new SpriteBatch

  private def render() : Unit = {
    Gdx.gl.glClearColor(0,0,0,1)
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
    batch.begin()
    store.components.foreach(_ render batch)
    batch.end()
  }

  def asScreen : Screen = {
    lifecycleEventEmitter
  }

}
