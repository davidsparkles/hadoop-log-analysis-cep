package de.davidsparkles

import com.typesafe.config.ConfigFactory
import de.davidsparkles.core.finite_state_machine.Event
import de.davidsparkles.core.agent.{AgentMonitor, _}
import de.davidsparkles.core.io.{Input, Output}
import de.davidsparkles.io.input.{FileReader, FileTailer}
import de.davidsparkles.io.output.{ConsoleOutput, Discarder, FileOutput, KafkaOutput}
import de.davidsparkles.parsing.LogEntryParser
import de.davidsparkles.runner.MainThreadSleep
import org.apache.logging.log4j.scala.Logging


object AgentMain extends App with Logging {

  override def main(args: Array[String]): Unit = {

    val config = ConfigFactory.load("agent.conf")

    val inputConfig = config.getConfig("agent.input")
    val input: Input[String] = inputConfig.getString("type") match {
      case "FILE_READER" => new FileReader(inputConfig, false)
      case "FILE_TAILER" => new FileTailer(inputConfig)
      case inputType =>
        logger.error(s"The configured input type '$inputType' is not valid.")
        return
    }

    val parser: Parser[String, Event] = new LogEntryParser(config)

    val outputConfig = config.getConfig("agent.output")
    val output: Output[Event] = outputConfig.getString("type") match {
      case "KAFKA" => new KafkaOutput(outputConfig)
      case "FILE" => new FileOutput(outputConfig, true)
      case "DISCARDER" => new Discarder()
      case "CONSOLE" => new ConsoleOutput(true)
      case outputType =>
        logger.error(s"The configured output type '$outputType' is not valid.")
        return
    }

    val monitor = new AgentMonitor()

    val agent: Agent[String, Event] = new Agent(input, parser, output, monitor)

    logger.info("Start the agent")

    val agentThread = new Thread(agent)
    agentThread.start()

    config.getString("agent.stopCondition") match {
      case "INFINITY" => MainThreadSleep.tillInfinity()
      case "TIMEOUT" => MainThreadSleep.tillTimeoutExpires(config.getDuration("agent.maxRunningTime").toMillis)
      case "SYSTEM_IN_STOP" => MainThreadSleep.tillSystemInStop(config.getString("agent.stopCommand"))
      case stopCondition =>
        logger.error(s"The configured stop condition '$stopCondition' is not valid.")
    }

    logger.info("Stop the agent")
    agent.stop()
  }
}