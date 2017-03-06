package com.onsightgames.scalalibgdx

import com.badlogic.gdx.backends.android._
import android.os.Bundle

class Main extends AndroidApplication {
  override def onCreate(savedInstanceState: Bundle) : Unit = {
    super.onCreate(savedInstanceState)
    val config = new AndroidApplicationConfiguration
    config.useAccelerometer = false
    config.useCompass = false
    config.useWakelock = true
    config.hideStatusBar = true
    initialize(new ScalaLibGDX, config)
  }
}