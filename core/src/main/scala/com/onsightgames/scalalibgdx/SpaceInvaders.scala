package com.onsightgames.scalalibgdx

import akka.typed.scaladsl.Actor.{Same, Stateful, _}
import akka.typed.{ActorRef, Behavior, _}
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.{Gdx, Screen}
import com.onsightgames.scalalibgdx.events.LifecycleManager
import com.onsightgames.scalalibgdx.events.LifecycleManager._
import com.onsightgames.scalalibgdx.libgdx.{Rectangle, Vector2}

object SpaceInvaders extends HasLogger {
  override val LogId : String = "SpaceInvaders"

  private val screen = Rectangle(
    bottomLeft   = Vector2.Zero,
    dimensions = Vector2(
      x = Gdx.graphics.getWidth.toFloat,
      y = Gdx.graphics.getHeight.toFloat
    )
  )

  // entity to specify its own events, register against :
  //  -- lifecycle events
  //  -- input events


  trait EntityEvents
  case class UpdateEntity(render : LifecycleManager.RenderEntity) extends EntityEvents

  private def entity(
    lifecycleManagerRef : ActorRef[LifecycleManager.Register],
    data                : Int
  ) : Behavior[EntityEvents] = Stateful[EntityEvents] (
    behavior = { (_, msg) =>
      msg match {
        case UpdateEntity(render) =>
          println(s"Render entity! $render")
          Same
      }
    },
    signal = { (ctx, sig) =>
      sig match {
        case PreStart =>
          val renderRef = ctx.spawnAdapter{
            render : RenderEntity => UpdateEntity(render)
          }
          lifecycleManagerRef ! LifecycleManager.RegisterRender(renderRef)
          Same
      }
    }
  )



  private val components : Set[Component[_ <: Entity]] = Level.first(screen).components

  private val store = new Store(components)


  private val keyboardEventEmitter      = new KeyboardEventEmitter(store.dispatch)
  private val boundaryCollisionDetector = new BoundaryCollisionDetector
  private val collisionDetector         = new CollisionDetector



  private def render() : Unit = {
    Gdx.gl.glClearColor(0,0,0,1)
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
    batch.begin()
    store.components.foreach(_ render batch)
    batch.end()
  }

  def update(updateEvent : Update) : Unit = {
    store.dispatch(updateEvent)
    boundaryCollisionDetector
      .run(screen, store.getState)
      .foreach(store.dispatch)

    collisionDetector
      .run(store.getState)
      .foreach(store.dispatch)
  }

  def start(
    lifecycleManager : ActorRef[LifecycleManager.Register]
  ) : Unit = {
    keyboardEventEmitter.start()
  }
}