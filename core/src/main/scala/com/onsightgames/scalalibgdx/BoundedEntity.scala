package com.onsightgames.scalalibgdx

import com.onsightgames.scalalibgdx.libgdx.Rectangle

trait BoundedEntity extends Entity {

  val boundingBox : Rectangle

  def isLeftOf(hasBoundingBox : BoundedEntity) : Boolean = {
    boundingBox.x < hasBoundingBox.boundingBox.x
  }

  def isRightOf(hasBoundingBox : BoundedEntity) : Boolean = {
    boundingBox.x > hasBoundingBox.boundingBox.x
  }
}
