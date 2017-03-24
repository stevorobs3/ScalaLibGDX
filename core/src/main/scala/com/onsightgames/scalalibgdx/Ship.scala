package com.onsightgames.scalalibgdx

import java.util.UUID

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector2
import com.onsightgames.scalalibgdx.experiments.GameObject
import com.onsightgames.scalalibgdx.spaceinvaders.objects.Alien

object Ship {

  val Dampening = 0.03f
  val AccelerationScale = 0.3f

  val atStartingPosition = Ship(
    new Vector2(Gdx.graphics.getWidth / 2f, Gdx.graphics.getHeight / 10f),
    Alien.SimpleAlienScale,
    UUID.randomUUID()
  )
}

case class Ship private (
  startingPosition : Vector2,
  scale            : Float,
  override val id  : UUID
)
  extends KeyboardAdapter.Listener
  with Logging
  with GameObject {

  import Ship._

  val LogId = "Ship"

  private val texture = new Texture("ship.png")

  private val width  = texture.getWidth * scale
  private val height = texture.getHeight * scale

  private var position = startingPosition
  private var velocity = new Vector2(0f, 0f)
  private var acceleration = new Vector2(0f, 0f)

  override def updateXAcceleration(x: Float): Unit = {
    acceleration = acceleration.set(AccelerationScale * x, acceleration.y)
  }

  override def updateYAcceleration(y: Float): Unit = {
    acceleration = acceleration.set(acceleration.x, AccelerationScale * y)
  }

  def update() : Unit = {
    velocity = dampen(velocity.add(acceleration))

    position = position.add(velocity)
    position = new Vector2(
      positiveModulus(position.x, Gdx.graphics.getWidth.toFloat),
      positiveModulus(position.y, Gdx.graphics.getHeight.toFloat)
    )
  }

  def render(batch : SpriteBatch) : Unit = {
    batch.draw(texture, position.x, position.y, width, height)
  }

  private def positiveModulus(dividend : Float, divisor : Float) : Float = {
    ((dividend % divisor) + divisor) % divisor
  }

  private def dampen(vector : Vector2) : Vector2 = {
    vector.set(dampen(vector.x), dampen(vector.y))
  }

  private def dampen(scalar : Float) : Float = {
    val unsignedDampened = math.abs(scalar) * (1f - Dampening)
    if (unsignedDampened < Dampening) {
      0
    } else if (scalar > 0) {
      unsignedDampened
    } else {
      -unsignedDampened
    }
  }
}
