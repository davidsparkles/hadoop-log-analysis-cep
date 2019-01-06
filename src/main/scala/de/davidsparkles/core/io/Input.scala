package de.davidsparkles.core.io

trait Input[InputType] {

  def receive: Iterator[InputType]

  def close(): Unit = {}
}
