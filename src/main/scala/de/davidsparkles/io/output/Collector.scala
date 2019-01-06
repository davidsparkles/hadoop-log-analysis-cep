package de.davidsparkles.io.output

import de.davidsparkles.core.io.Output

import scala.collection.mutable.ArrayBuffer
import scala.reflect.ClassTag

class Collector[Type] extends Output[Type] {

  private val entries = ArrayBuffer[Type]()

  override def output(entry: Type): Unit = {
    entries += entry
  }

  def getEntries(implicit c: ClassTag[Type]): Array[Type] = entries.toArray
}
