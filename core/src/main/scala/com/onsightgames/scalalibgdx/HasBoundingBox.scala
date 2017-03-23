package com.onsightgames.scalalibgdx

import com.onsightgames.scalalibgdx.libgdx.Rectangle

trait HasBoundingBox {

  val boundingBox : Rectangle

  def isLeftOf(hasBoundingBox : HasBoundingBox) : Boolean = {
    boundingBox.x < hasBoundingBox.boundingBox.x
  }

  def isRightOf(hasBoundingBox : HasBoundingBox) : Boolean = {
    boundingBox.x > hasBoundingBox.boundingBox.x
  }
}
