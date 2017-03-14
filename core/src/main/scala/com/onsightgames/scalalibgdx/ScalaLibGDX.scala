package com.onsightgames.scalalibgdx

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.{GL20, Texture}
import com.badlogic.gdx.{Game, Gdx}

object ScalaLibGDX {
  val Width = 480
  val Height = 800
  val Title = "ScalaLibGDX"

  // pluck ^ from global configuration
  lazy val Instance : ScalaLibGDX = new ScalaLibGDX
}

class ScalaLibGDX extends Game
 with HasLogger {
  val LogId = "ScalaLibGDX"

  lazy val sprite = new Texture("libgdxlogo.png")
  lazy val batch  = new SpriteBatch

  def setScreen[W,E](newScreen : SinglePlayerScreen[W,E]) : Unit = {
    setScreen(newScreen.screen)
  }

  override def create(): Unit = {}
  override def render(): Unit = {
    Gdx.gl.glClearColor(1,1,1,1)
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
    batch.begin()
    val x = (Gdx.graphics.getWidth - sprite.getWidth) / 2f
    val y = (Gdx.graphics.getHeight - sprite.getHeight) / 2f
    batch.draw(sprite, x, y)
    batch.end()
  }
}