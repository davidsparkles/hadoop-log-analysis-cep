package de.davidsparkles.model.container_memory_usage

import com.typesafe.config.Config
import de.davidsparkles.core.finite_state_machine.{Event, FiniteStateMachine, State, Transition}
import de.davidsparkles.logevents.LogEventWithParameters

import scala.collection.mutable.ArrayBuffer


class ContainerAllocatedTransition(override val from: State, override val to: State,
                                   override val transitionConfig: Config) extends Transition[LogEventWithParameters] {

  override val transitionIdentifier: String = "containerAllocated"

  override def evaluateCustom(event: LogEventWithParameters, finiteStateMachine: FiniteStateMachine[LogEventWithParameters]): Boolean = {
    if (event.parameters("operation") == "AM Allocated Container") {
      val applicationId = event.parameters.getOrElse("applicationId", null)
      val containerId = event.parameters.getOrElse("containerId", null)
      if (applicationId != null && containerId != null &&
        applicationId == finiteStateMachine.properties("applicationId")) {
        finiteStateMachine.properties("containerIds").asInstanceOf[ArrayBuffer[String]] += containerId
        return true
      }
    }
    false
  }
}
