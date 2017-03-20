package com.onsightgames.scalalibgdx

import com.badlogic.gdx.Input.Keys
import com.badlogic.gdx.InputAdapter

object KeyboardAdapter {

  private val XDirectionKeys = Map(Keys.LEFT -> -1f, Keys.RIGHT -> +1f)
  private val YDirectionKeys = Map(Keys.DOWN -> -1f, Keys.UP -> +1f)

  trait Listener {
    def updateXAcceleration(x : Float) : Unit
    def updateYAcceleration(y : Float) : Unit
  }

}

class KeyboardAdapter(listener : KeyboardAdapter.Listener) extends InputAdapter {

  import KeyboardAdapter._

  override def keyDown(keycode : Int) : Boolean = {
    val x = calculateMagnitude(XDirectionKeys, keycode)
    if (x != 0) {
      listener updateXAcceleration x
      true
    }
    else {
      val y = calculateMagnitude(YDirectionKeys, keycode)
      if (y != 0) {
        listener updateYAcceleration y
        true
      }
      else {
        false
      }
    }
  }

  override def keyUp(keycode : Int) : Boolean = {
    val x = calculateMagnitude(XDirectionKeys, keycode)
    if (x != 0) {
      listener updateXAcceleration 0
      true
    }
    else {
      val y = calculateMagnitude(YDirectionKeys, keycode)
      if (y != 0) {
        listener updateYAcceleration 0
        true
      }
      else {
        false
      }
    }
  }

  private def calculateMagnitude(keyMap : Map[Int, Float], pressedKey : Int) : Float = {
    keyMap
      .collectFirst { case (key, magnitude) if key == pressedKey => magnitude }
      .getOrElse(0f)
  }

}
