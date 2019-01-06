package de.davidsparkles.util

import com.typesafe.config.Config
import de.davidsparkles.core.finite_state_machine.Event

import scala.collection.mutable.ArrayBuffer


class StreamWindowSort[EventType >: Null <: Event](private val maxBufferSize: Int) {

  def this(config: Config) {
    this(config.getInt("maxBufferSize"))
  }

  private val buffer = ArrayBuffer[EventType]()

  def getNextEvent(event: EventType): EventType = {
    buffer += event
    if (buffer.size > maxBufferSize) {
      val nextEvent = buffer.minBy(_.timestamp)
      buffer -= nextEvent
      nextEvent
    }
    else null
  }

  def getNextEvent: EventType = {
    if (buffer.nonEmpty) {
      val nextEvent = buffer.minBy(_.timestamp)
      buffer -= nextEvent
      nextEvent
    }
    else null
  }

  def nonEmpty: Boolean = buffer.nonEmpty
}
