package de.davidsparkles.model

import de.davidsparkles.core.finite_state_machine._
import de.davidsparkles.io.output.ConsoleOutput


/*
class CreateCountModel (count: Int, override val timewindow: Long = 1000,
                        override val output: ResultOutput = new ConsoleOutput())
  extends FiniteStateMachineModel {

  private val state0 = State.create()
  private val state1 = State.create()
  override val timewindowExceededState: State = State.create("Timewindow Exceeded")

  private def predicate0(event: Event, partialPattern: Array[(Int, Transition, Event)]): Boolean = {
    if (partialPattern == null || partialPattern.length == 0) {
      event.logFile == "HDFS-AUDIT" && event.properties("cmd") == "create"
    } else {
      val first = partialPattern.head._3
      event.logFile == "HDFS-AUDIT" && event.properties("cmd") == "create" &&
        first.properties("src") == event.properties("src") && partialPattern.length < count
    }
  }
  private val transition0 = Transition.create(state0, state0, predicate0)

  private def predicate1(event: Event, partialPattern: Array[(Int, Transition, Event)]): Boolean = {
    if (partialPattern == null || partialPattern.length == 0) {
      false
    } else {
      val first = partialPattern.head._3
      event.logFile == "HDFS-AUDIT" && event.properties("cmd") == "create" &&
        first.properties("src") == event.properties("src")
    }
  }
  private val transition1 = Transition.create(state0, state1, predicate1)

  override val states: Array[State] = Array(state0, state1, timewindowExceededState)
  override val startState: State = state0
  override val endStates: Array[State] = Array(state1, timewindowExceededState)
  override val transitions: Array[Transition] = Array(transition0, transition1)
  override val startTransitions: Array[Transition] = transitions.filter(_.from == startState)

  override def aggregate(partialPattern: Array[(Int, Transition, Event)]): Any = {
    val first = partialPattern.head._3
    val last = partialPattern.last._3

    val result = (
      "Create Count Pattern",
      count,
      timewindow,
      first.properties("src"),
      first.timestamp,
      last.timestamp
    )
    result
  }
}
*/
