package de.davidsparkles.core.io

trait Output[Type] {

  def output(entry: Type): Unit

  def close(): Unit = { }
}
