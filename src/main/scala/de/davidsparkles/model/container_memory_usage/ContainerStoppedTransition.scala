package de.davidsparkles.model.container_memory_usage

import com.typesafe.config.Config
import de.davidsparkles.core.finite_state_machine.{Event, FiniteStateMachine, State, Transition}
import de.davidsparkles.logevents.LogEventWithParameters

import scala.collection.mutable.ArrayBuffer


class ContainerStoppedTransition(override val from: State, override val to: State,
                                 override val transitionConfig: Config) extends Transition[LogEventWithParameters] {

  override val transitionIdentifier: String = "containerStopped"

  override def evaluateCustom(event: LogEventWithParameters, finiteStateMachine: FiniteStateMachine[LogEventWithParameters]): Boolean = {
    val containerId = event.parameters.getOrElse("containerId", null)
    val containerIds = finiteStateMachine.properties("containerIds").asInstanceOf[ArrayBuffer[String]]
    containerId != null && containerIds.contains(containerId)
  }
}