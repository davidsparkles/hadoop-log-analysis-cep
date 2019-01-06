package de.davidsparkles.core.agent

import java.util.Date

import org.apache.logging.log4j.scala.Logging

class AgentMonitor extends Logging {

  var numberOfReceivedEntries: Long = 0L
  var numberOfEmittedEntries: Long = 0L

  private var time = 0L

  def increaseNumberOfReceivedEntries(): Unit = numberOfReceivedEntries += 1

  def increaseNumberOfEmittedEntries(): Unit = numberOfEmittedEntries += 1

  def resetTimeAndNumbers(): Unit = {

    numberOfReceivedEntries = 0
    numberOfEmittedEntries = 0
    time = new Date().getTime
  }

  def evaluateThroughput(): Unit = {

    if (numberOfReceivedEntries == 0) return
    val endTime = new Date().getTime - time
    val elapsedTime = if (endTime != 0) endTime else 1
    val throughputReceivedEntries = numberOfReceivedEntries.toDouble / elapsedTime * 1000
    val throughputEmittedEntries = numberOfEmittedEntries.toDouble / elapsedTime * 1000
    logger.info(s"Processing took $elapsedTime ms for $numberOfReceivedEntries received entries and " +
      s"$numberOfEmittedEntries emitted entries with respective throughputs: $throughputReceivedEntries   " +
      s"$throughputEmittedEntries")
  }
}
