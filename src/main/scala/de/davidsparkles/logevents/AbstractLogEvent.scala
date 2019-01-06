package de.davidsparkles.logevents

import de.davidsparkles.core.finite_state_machine.Event

abstract class AbstractLogEvent extends Event {
  val logFile: String
  val server: String
  val severity: String
  val loggingClass: String
  val lineNumber: Int
}
