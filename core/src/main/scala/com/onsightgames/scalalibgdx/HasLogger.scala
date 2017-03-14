package com.onsightgames.scalalibgdx

import com.badlogic.gdx.Gdx

trait HasLogger {
  val LogId : String

  def debug(message : String) : Unit = {
    Gdx.app.debug(LogId, message)
  }

  def info(message : String) : Unit = {
    Gdx.app.log(LogId, message)
  }

  def error(message : String) : Unit = {
    Gdx.app.error(LogId, message)
  }
}