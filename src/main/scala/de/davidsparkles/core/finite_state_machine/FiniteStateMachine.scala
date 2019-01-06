package de.davidsparkles.core.finite_state_machine

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

case class FiniteStateMachine[EventType <: Event](var state: State,
                                                  partialPattern: ListBuffer[MatchedEvent[EventType]],
                                                  var properties: mutable.Map[String, Any])

