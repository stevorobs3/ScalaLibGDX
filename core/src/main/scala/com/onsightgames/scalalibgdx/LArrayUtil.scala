package com.onsightgames.scalalibgdx

import com.badlogic.gdx.utils.{Array => LArray}

object LArrayUtil {
  implicit class ArrayExtension[T](items : List[T]) {
    def toLArray : LArray[T] = {
      val array = new LArray[T](items.length)
      items.foreach(item => array.add(item))
      array
    }
  }

  implicit class ListExtension[T, R](items : LArray[T]) {
    // TODO: do this with the collection builder library
    def map(f : T => R) : LArray[R] = {
      val newArray = new LArray[R](items.size)
      (0 to items.size).foreach{i =>
        newArray.set(i, f(items.get(i)))
      }
      newArray
    }
  }
}