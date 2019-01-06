package de.davidsparkles

import com.typesafe.config.ConfigFactory
import de.davidsparkles.core.agent.Agent
import de.davidsparkles.core.finite_state_machine.Event
import de.davidsparkles.core.io.Input
import de.davidsparkles.io.input.FileReader
import de.davidsparkles.logevents.{LogEventWithMessage, LogEventWithParameters}

object Test2 extends App {
  private val config = ConfigFactory.load("patternDetection.conf")

  private val inputConfig = config.getConfig("patternDetection.input")
  private val input: Input[Event] = new FileReader[Event](inputConfig, true)

  input.receive.foreach(event => println(event.timestamp))
}
