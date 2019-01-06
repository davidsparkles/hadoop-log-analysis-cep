package de.davidsparkles.model.container_memory_usage

import com.typesafe.config.Config
import de.davidsparkles.core.finite_state_machine.{Event, FiniteStateMachine, State, Transition}
import de.davidsparkles.logevents.LogEventWithParameters

import scala.collection.mutable.{ArrayBuffer, Map}


class ApplicationStartedTransition(override val from: State, override val to: State,
                                   override val transitionConfig: Config) extends Transition[LogEventWithParameters] {

  override val transitionIdentifier: String = "applicationStarted"

  override def evaluateCustom(event: LogEventWithParameters, finiteStateMachine: FiniteStateMachine[LogEventWithParameters]): Boolean = {
    if (event.parameters("operation") == "Submit Application Request") {
      val applicationId = event.parameters.getOrElse("applicationId", null)
      if (applicationId == null) return false
      finiteStateMachine.properties("applicationId") = applicationId
      true
    } else false
  }
}
