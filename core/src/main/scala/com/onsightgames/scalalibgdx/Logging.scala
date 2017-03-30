package com.onsightgames.scalalibgdx

import com.badlogic.gdx.Gdx

trait Logging {
  private val LogId : String = getClass.toString.split('.').toList.last

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