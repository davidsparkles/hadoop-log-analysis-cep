package de.davidsparkles.core.agent

import de.davidsparkles.core.io.{Input, Output}
import org.apache.logging.log4j.scala.Logging

class Agent[InputType, OutputType](input: Input[InputType], parser: Parser[InputType, OutputType],
                                   output: Output[OutputType], monitor: AgentMonitor = null)
  extends Runnable with Logging {

  private var running = false
  private var timeout: Long = 1L

  def setTimeout(timeout: Long): Unit = this.timeout = timeout

  override def run(): Unit = {
    running = true
    while(running) {
      if (monitor != null) monitor.resetTimeAndNumbers()
      input.receive
        .map(increaseNumberOfReceivedEntries)
        .map(parser.parse)
        .filter(_ != null)
        .map(increaseNumberOfEmittedEntries)
        .foreach(output.output)
      if (monitor != null) monitor.evaluateThroughput()
      Thread.sleep(timeout)
    }
  }

  def stop(): Unit = running = false

  private def increaseNumberOfReceivedEntries(entry: InputType): InputType = {
    if (monitor != null) monitor.increaseNumberOfReceivedEntries()
    entry
  }

  private def increaseNumberOfEmittedEntries(entry: OutputType): OutputType = {
    if (monitor != null) monitor.increaseNumberOfEmittedEntries()
    entry
  }
}
