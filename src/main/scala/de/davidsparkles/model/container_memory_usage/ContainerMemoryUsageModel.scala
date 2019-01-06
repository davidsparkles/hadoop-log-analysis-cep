package de.davidsparkles.model.container_memory_usage

import com.typesafe.config.Config
import de.davidsparkles.core.finite_state_machine._
import de.davidsparkles.core.io.Output
import de.davidsparkles.io.output.ConsoleOutput
import de.davidsparkles.logevents.LogEventWithParameters
import de.davidsparkles.model.container_memory_usage.ContainerMemoryUsageResult.{Application, Container, MemoryObservation}

import scala.collection.mutable
import scala.collection.mutable.{ArrayBuffer, ListBuffer}

class ContainerMemoryUsageModel(override val transitionConfig: Config,
                                override val timewindow: Long = 100)
  extends FiniteStateMachineModel {

  override type EventType = LogEventWithParameters
  override type ResultType = ContainerMemoryUsageResult.Application

  override val startState: State = State.create("Start")
  private val applicationIsRunningState = State.create("YARN application is running")
  private val applicationFinishedState = State.create("YARN application finished")
  override val timewindowExceededState: State = State.create("Timewindow exceeded")

  private val applicationStartedTransition = new ApplicationStartedTransition(startState,
    applicationIsRunningState, transitionConfig)
  private val containerAllocatedTransition = new ContainerAllocatedTransition(applicationIsRunningState,
    applicationIsRunningState, transitionConfig)
  private val containerStartedTransition = new ContainerStartedTransition(applicationIsRunningState,
    applicationIsRunningState, transitionConfig)
  private val containerIsMonitoredTransition = new ContainerIsMonitoredTransition(applicationIsRunningState,
    applicationIsRunningState, transitionConfig)
  private val containerStoppedTransition = new ContainerStoppedTransition(applicationIsRunningState,
    applicationIsRunningState, transitionConfig)
  private val applicationFinishedTransition = new ApplicationFinishedTransistion(applicationIsRunningState,
    applicationFinishedState, transitionConfig)

  override val states: Array[State] = Array(startState, applicationIsRunningState, applicationFinishedState, timewindowExceededState)
  override val endStates: Array[State] = Array(applicationFinishedState, timewindowExceededState)
  override val transitions: Array[Transition[EventType]] = Array(applicationStartedTransition,
    containerAllocatedTransition, containerStartedTransition, containerIsMonitoredTransition,
    containerStoppedTransition, applicationFinishedTransition)
  override val startTransitions: Array[Transition[EventType]] = transitions.filter(_.from == startState)

  override protected def aggregate(finiteStateMachine: FiniteStateMachine[EventType]): ContainerMemoryUsageResult.Application = {

    val partialPattern = finiteStateMachine.partialPattern
    val properties = finiteStateMachine.properties

    val startTransition = partialPattern.find(_.transition == applicationStartedTransition).orNull
    val stopTransition = partialPattern.find(_.transition == applicationFinishedTransition).orNull
    if (startTransition == null || stopTransition == null) return null
    val applicationId = startTransition.event.parameters.getOrElse("applicationId", null)
    val user = startTransition.event.parameters.getOrElse("user", null)
    if (applicationId == null || user == null) return null
    val applicationStartTime = startTransition.event.timestamp
    val applicationEndTime = stopTransition.event.timestamp

    val containerIds: Array[String] = properties("containerIds").asInstanceOf[ArrayBuffer[String]].toArray
    val containers: Array[Container] = containerIds.map(containerId => {
      val containerStartEntry = partialPattern.find(entry => entry.transition == containerStartedTransition &&
        entry.event.parameters.getOrElse("containerId", null) == containerId).orNull
      val server = if (containerStartEntry == null) "" else containerStartEntry.event.server
      val observations: ListBuffer[MemoryObservation] = partialPattern.filter(_.transition == containerIsMonitoredTransition)
        .filter(_.event.parameters.getOrElse("containerId", null) == containerId)
        .map(item => {
          val actualPhysicalMemory = item.event.parameters.getOrElse("actualPhysicalMemory", "")
          val configuredPhysicalMemory = item.event.parameters.getOrElse("configuredPhysicalMemory", "")
          val actualVirtualMemory = item.event.parameters.getOrElse("actualVirtualMemory", "")
          val configuredVirtualMemory = item.event.parameters.getOrElse("configuredVirtualMemory", "")
          MemoryObservation(item.event.timestamp, actualPhysicalMemory, configuredPhysicalMemory, actualVirtualMemory,
            configuredVirtualMemory)
        })
      val containerStartTime = partialPattern.filter(_.transition == containerStartedTransition).find(item => {
        item.event.parameters.getOrElse("containerId", null) == containerId
      }).map(_.event.timestamp).getOrElse(0l)
      val containerEndTime = partialPattern.filter(_.transition == containerStoppedTransition).find(item => {
        item.event.parameters.getOrElse("containerId", null) == containerId
      }).map(_.event.timestamp).getOrElse(0l)
      Container(containerId, server, containerStartTime, containerEndTime, observations.toArray)
    })
    Application(applicationId.asInstanceOf[String], user, applicationStartTime, applicationEndTime, containers)
  }

  override protected def createMachine(event: EventType): Unit = {

    val newFiniteStateMachine = FiniteStateMachine[EventType](startState,
      ListBuffer[MatchedEvent[this.EventType]](), mutable.Map[String, Any]("applicationId" -> null, "containerIds" -> ArrayBuffer[String]()))
    if (startTransitions.exists(_.evaluate(event, newFiniteStateMachine))) addFiniteStateMachine(newFiniteStateMachine)
  }

  override def toString: String = s"ContainerMemoryUsageModel(Output=$output, Timewindow=$timewindow)"

  override var output: Output[Application] = _
}