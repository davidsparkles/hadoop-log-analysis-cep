package de.davidsparkles.core.finite_state_machine

case class MatchedEvent[EventType <: Event] (event: EventType, transition: Transition[EventType])
