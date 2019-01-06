package de.davidsparkles

import com.typesafe.config.ConfigFactory
import de.davidsparkles.core.finite_state_machine.Event
import de.davidsparkles.core.agent.Agent
import de.davidsparkles.io.input.{FileReader, StubInput}
import de.davidsparkles.io.output.{Collector, ConsoleOutput}
import de.davidsparkles.logevents.LogEventWithParameters
import de.davidsparkles.model.container_memory_usage.ContainerMemoryUsageModel
import de.davidsparkles.parsing.LogEntryParser
import de.davidsparkles.runner.{MainThreadSleep, PatternDetectionRunner}
import org.apache.logging.log4j.scala.Logging

object Test extends App with Logging {

  val nodeManagerAgentConfig = ConfigFactory.load("nodeManagerAgent.conf")
  val resourceManagerAuditAgentConfig = ConfigFactory.load("resourceManagerAuditAgent.conf")


  // val topic = agentConfig.getString("kafka.topic")
  // val bootstrapServer = agentConfig.getString("kafka.bootstrapServer")
  // val sender = new KafkaProducerWrapper(topic, bootstrapServer)
  // logger.info(s"Use KafkaProducer as ouput with topic $topic and bootstrapServer $bootstrapServer")

  val input = new FileReader[String](resourceManagerAuditAgentConfig.getConfig("agent.input"), false)
  val parser = new LogEntryParser(resourceManagerAuditAgentConfig)
  val sender = new Collector[Event]()
  // val sender = new ConsoleOutput()

  // val nodeManagerAgent: Agent[String, Event] = new Agent(
    // new LogFileReader(nodeManagerAgentConfig.getConfig("agent.input")),
    // new LogEntryParser(nodeManagerAgentConfig), sender)
  val resourceManagerAuditAgent: Agent[String, Event] = new Agent(input, parser, sender)


  // nodeManagerAgent.start()
  new Thread(resourceManagerAuditAgent).start()

  MainThreadSleep.tillTimeoutExpires(4000)

  val events = sender.getEntries.sortBy(_.timestamp)
  events.foreach(event => {
    val e = event.asInstanceOf[LogEventWithParameters]
    println(e.lineNumber, e.parameters)
  })

  resourceManagerAuditAgent.stop()

  // val eventSaver = new FileOutput[Event]("./output/events.out")
  // events.foreach(eventSaver.send(_))

/*
  val patternDetectionConfig = ConfigFactory.load("patternDetection.conf")
  val transitionConfig = patternDetectionConfig.getConfig("patternDetection.transitions")

  // val receiver = new FakeEventReceiver(events)
  val receiver = new FileEventReceiver("../data/spark-yarn-perm/yarn-events.json")
  val output = new ConsoleOutput(asJson = true)

  val runner = new PatternDetectionRunner(receiver)
  runner.addFiniteStateMachineModel(new ContainerMemoryUsageModel(transitionConfig, output, 60 * 60 * 1000))
  logger.info(s"Added models to the CEPRunner: ${runner.finiteStateMachineModels.map(_.toString).mkString(", ")}")

  runner.runOnce()
  */
}
