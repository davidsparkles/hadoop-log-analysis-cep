package de.davidsparkles.core.finite_state_machine

import com.typesafe.config.Config
import de.davidsparkles.logevents.AbstractLogEvent

abstract class Transition[EventType <: Event] {

  val transitionConfig: Config
  val transitionIdentifier: String
  val from: State
  val to: State

  final def evaluate(event: EventType, finiteStateMachine: FiniteStateMachine[EventType]): Boolean =
    matchesLogFileClassAndLine(event) && evaluateCustom(event, finiteStateMachine)

  private def matchesLogFileClassAndLine(eventSimple: EventType): Boolean = {
    val event = eventSimple.asInstanceOf[AbstractLogEvent]
    val configuredLogFile = getLogFile(transitionIdentifier)
    val configuredLoggingClass = getLoggingClass(transitionIdentifier)
    val configureLineNumber = getLineNumber(transitionIdentifier)
    event.logFile == configuredLogFile && event.loggingClass == configuredLoggingClass &&
      event.lineNumber == configureLineNumber
  }

  def evaluateCustom(event: EventType, finiteStateMachine: FiniteStateMachine[EventType]): Boolean

  private def getLogFile(transitionIdentifier: String): String = {
    transitionConfig.getConfig(transitionIdentifier).getString("logFile")
  }

  private def getLineNumber(transitionIdentifier: String): Int = {
    transitionConfig.getConfig(transitionIdentifier).getInt("lineNumber")
  }

  private def getLoggingClass(transitionIdentifier: String): String = {
    transitionConfig.getConfig(transitionIdentifier).getString("loggingClass")
  }
}