package de.davidsparkles.runner

import de.davidsparkles.core.finite_state_machine._
import de.davidsparkles.core.io.Input
import de.davidsparkles.logevents.LogEventWithParameters
import de.davidsparkles.util.StreamWindowSort
import org.apache.logging.log4j.scala.Logging

import scala.collection.mutable.ArrayBuffer

class PatternDetectionRunner[EventType >: Null <: Event](input: Input[EventType], maxBufferSize: Int = 0, timeout: Long = 1000L)
  extends Runnable with Logging {

  private val sort = new StreamWindowSort[EventType](maxBufferSize)
  implicit val finiteStateMachineModels: ArrayBuffer[FiniteStateMachineModel] = ArrayBuffer[FiniteStateMachineModel]()
  private var running: Boolean = false

  def stop(): Unit = {
    running = false
    while (sort.nonEmpty) processEvent(sort.getNextEvent)
  }

  def addFiniteStateMachineModel(model: FiniteStateMachineModel): Unit = finiteStateMachineModels += model

  def run(): Unit = {
    running = true
    while (running) {
      val events = input.receive
      processEvents(events)
      Thread.sleep(timeout)
    }
  }

  def processEvents(eventsIterator: Iterator[EventType]): Unit = {
    val events = eventsIterator.toList
    logger.debug(s"Received ${events.length} events")
    events.filter(_ != null).foreach(event => {
      val nextEvent = sort.getNextEvent(event)
      if (nextEvent != null) processEvent(nextEvent)
    })
  }

  private def processEvent(event: EventType): Unit = {
    for (model <- finiteStateMachineModels) {
      model.processEvent(event.asInstanceOf[model.EventType])
    }
  }
}
