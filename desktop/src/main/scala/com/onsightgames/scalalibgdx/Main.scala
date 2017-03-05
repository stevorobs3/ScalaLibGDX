package com.onsightgames.scalalibgdx

import com.badlogic.gdx.backends.lwjgl.{LwjglApplication, LwjglApplicationConfiguration}

object Main extends App {
    val cfg = new LwjglApplicationConfiguration
    cfg.title = ScalaLibGDX.Title
    cfg.height = ScalaLibGDX.Width
    cfg.width = ScalaLibGDX.Height
    cfg.forceExit = false
    new LwjglApplication(new ScalaLibGDX, cfg)
}