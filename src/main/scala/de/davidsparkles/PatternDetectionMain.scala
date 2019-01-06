package de.davidsparkles

import java.lang.management.ManagementFactory
import java.time.Duration

import com.typesafe.config.ConfigFactory
import de.davidsparkles.core.finite_state_machine.FiniteStateMachineModel
import de.davidsparkles.core.io.{Input, Output}
import de.davidsparkles.io.input.{FileReader, KafkaInput}
import de.davidsparkles.io.output.{ConsoleOutput, FileOutput}
import de.davidsparkles.logevents.LogEventWithParameters
import org.apache.logging.log4j.scala.Logging
import de.davidsparkles.model.container_memory_usage.{ContainerMemoryUsageModel, ContainerMemoryUsageResult}
import de.davidsparkles.runner.PatternDetectionRunner


object PatternDetectionMain extends App with Logging {

  override def main(args: Array[String]): Unit = {

    logMemory()
    logger.info("Load configurations and initialize the event input and result output")

    val config = ConfigFactory.load("patternDetection.conf")

    val transitionConfig = config.getConfig("patternDetection.transitions")
    val timeWindow = config.getDuration("patternDetection.timeWindow").toMillis
    val model: FiniteStateMachineModel = config.getString("patternDetection.pattern") match {
      case "CONTAINER_MEMORY_USAGE" => new ContainerMemoryUsageModel(transitionConfig, timeWindow)
      case modelIdentifier =>
        logger.error(s"The configured model '$modelIdentifier' is not valid.")
        return
    }

    val inputConfig = config.getConfig("patternDetection.input")
    val receiveTimeout = inputConfig.getDuration("timeout").toMillis
    val input: Input[LogEventWithParameters] = inputConfig.getString("type") match {
      case "FILE" => new FileReader[LogEventWithParameters](inputConfig, true)
      case "KAFKA" => new KafkaInput[LogEventWithParameters](inputConfig, true)
      case inputType =>
        logger.error(s"The configured input type '$inputType' is not valid.")
        return
    }

    val outputConfig = config.getConfig("patternDetection.output")
    val output: Output[model.ResultType] = outputConfig.getString("type") match {
      case "FILE" => new FileOutput[model.ResultType](outputConfig, true)
      case "CONSOLE" => new ConsoleOutput[model.ResultType](true)
      case outputType =>
        logger.error(s"The configured output type '$outputType' is not valid.")
        return
    }

    model.setOutput(output)

    val doSort = config.getBoolean("patternDetection.doSort")
    val maxBufferSize = config.getInt("patternDetection.maxBufferSize")
    val runner = if (doSort) new PatternDetectionRunner[LogEventWithParameters](input, maxBufferSize, receiveTimeout)
      else new PatternDetectionRunner[LogEventWithParameters](input)

    runner.addFiniteStateMachineModel(model)
    logger.info(s"Added models to the CEPRunner: ${runner.finiteStateMachineModels.map(_.toString).mkString(", ")}")

    val maxRunningTime: Duration = config.getDuration("patternDetection.maxRunningTime")
    val thread = new Thread(runner)

    // logMemory()
    logger.info("Start thread for pattern detection")

    thread.start()
    for (i <- 0 to (maxRunningTime.toMillis / 1000).toInt) {
      // logMemory()
      Thread.sleep(1000)
    }
    // logMemory()
    // Thread.sleep(maxRunningTime.toMillis)
    runner.stop()
    input.close()
    output.close()
    logMemory()
  }

  def logMemory(): Unit = {
    val before = getGcCount
    System.gc()
    while (getGcCount == before) {}
    val heapMem = ManagementFactory.getMemoryMXBean.getHeapMemoryUsage.getCommitted
    val nonHeapMem = ManagementFactory.getMemoryMXBean.getNonHeapMemoryUsage.getCommitted
    logger.info(s"HeapMem: $heapMem   NonHeapMem: $nonHeapMem")
  }

  def getGcCount = {
    var sum = 0
    import scala.collection.JavaConversions._
    for (b <- ManagementFactory.getGarbageCollectorMXBeans) {
      val count = b.getCollectionCount.toInt
      if (count != -1) sum += count
    }
    sum
  }

}
