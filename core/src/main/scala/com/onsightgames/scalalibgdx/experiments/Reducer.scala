package com.onsightgames.scalalibgdx.experiments

import java.util.UUID

import com.badlogic.gdx.Input.Keys
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Rectangle
import com.onsightgames.scalalibgdx.libgdx.Vector2

trait Reducer[State] {
  val initialState : State
  def reduce : PartialFunction[(State, Any), State]
}
// how to dynamically / compile time register for an event?


trait Renderer[State] {
  def render(state : State, batch : SpriteBatch) : Unit
}

// framework events
case class Update(delta : Float)
case class KeyboardEvent(keycode : Int)

// game specific events


// state data
case class Ship(position : Vector2) extends CollidableObject
case class Alien(position : Vector2) extends CollidableObject


class ShipReducer extends Reducer[Ship] {
  override val initialState: Ship = Ship(Vector2.Zero, alive = true)

  override def generateEvents(): Ship => List[Any]
  override def reduce(): PartialFunction[(Ship, Any), (Ship, List[Any])] = {
    case (ship, KeyboardEvent(keycode)) if keycode == Keys.A =>
      val newShip = ship.copy(
        position = ship.position + Vector2(0,1f)
      )
      newShip
    case (ship, Collision(collider : Alien)) =>
      if (ship.alive)
        // trigger event
      ship.copy(alive = false)
  }
}

trait CollidableObject{
  val id : UUID
  val events : Iterator[Any]
}

class Store {
  val collidableObjects : Iterator[CollidableObject]
  val reducers : Map[UUID,Reducer[CollidableObject]]

  var data : Iterator[CollidableObject]

  data = data.map{collidableObject =>
    val withoutEvents
    val events = List.empty[Any]
    events.foldLeft(collidableObject){
      case (previousCO, event) =>
        reducers(previousCO.id).reduce.applyOrElse((previousCO, event), _ => previousCO )
    }
  }


  reducers.reduce()
  // initial list of reducers

  //        root
  ///      /    \
  //     ship  alien fleet
  //           /     \
  //          boss   alien
  //          /
  //        minions

}


case class Collision(collider : CollidableObject, collider1 : CollidableObject)

class CollisionDetector {

  val collisionsToCheckFor : List[(Any,Any)]
  def checkForCollisions(collidableObjects : Iterator[CollidableObject] ) : Iterator[Collision]
}