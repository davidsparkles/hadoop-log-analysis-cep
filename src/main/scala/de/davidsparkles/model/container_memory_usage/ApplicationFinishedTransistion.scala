package de.davidsparkles.model.container_memory_usage

import com.typesafe.config.Config
import de.davidsparkles.core.finite_state_machine.{FiniteStateMachine, State, Transition}
import de.davidsparkles.logevents.LogEventWithParameters


class ApplicationFinishedTransistion(override val from: State, override val to: State,
                                     override val transitionConfig: Config) extends Transition[LogEventWithParameters] {

  override val transitionIdentifier: String = "applicationFinished"

  override def evaluateCustom(event: LogEventWithParameters, finiteStateMachine: FiniteStateMachine[LogEventWithParameters]): Boolean = {
    if (event.parameters("operation") == "Application Finished - Succeeded") {
      val applicationId = event.parameters.getOrElse("applicationId", null)
      applicationId != null && finiteStateMachine.properties("applicationId") == applicationId
    } else false
  }
}
