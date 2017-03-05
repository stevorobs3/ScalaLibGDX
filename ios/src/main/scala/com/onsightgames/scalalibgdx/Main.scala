package com.onsightgames.scalalibgdx

import com.badlogic.gdx.backends.iosrobovm.{IOSApplication, IOSApplicationConfiguration}
import org.robovm.apple.foundation.NSAutoreleasePool
import org.robovm.apple.uikit.UIApplication

class Main extends IOSApplication.Delegate {
  override protected def createApplication(): IOSApplication = {
    val config = new IOSApplicationConfiguration
    new IOSApplication(new ScalaLibGDX, config)
  }
}

object Main {
  def main(args: Array[String]) {
    val pool = new NSAutoreleasePool
    //scalastyle:off
    UIApplication.main(args, null, classOf[Main])
    //scalastyle:on
    pool.drain()
  }
}