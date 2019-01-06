package de.davidsparkles.model
/*
import de.davidsparkles.core.ConsoleOutput
import de.davidsparkles.core.finite_state_machine._
import de.davidsparkles.model.LeaseExpiredExceptionModel.ResultType


class LeaseExpiredExceptionModel (override val output: ResultOutput = new ConsoleOutput(),
                                  override val timewindow: Long = 100)
  extends FiniteStateMachineModel {

  private val state0 = State.create("Start")
  private val state1 = State.create()
  private val state2 = State.create()
  private val state3 = State.create()
  override val timewindowExceededState: State = State.create("Timewindow Exceeded")

  private def predicate0(event: Event, partialPattern: Array[(Int, Transition, Event)]): Boolean = {
    event.logFile == "HDFS-AUDIT" && event.properties("cmd") == "create"
  }
  private val transition0 = Transition.create(state0, state1, predicate0)

  private def predicate1(event: Event, partialPattern: Array[(Int, Transition, Event)]): Boolean = {
    val createEvent = partialPattern(0)._3
    event.logFile == "HDFS-AUDIT" && event.properties("cmd") == "delete" &&
      createEvent.properties("src").startsWith(event.properties("src"))
  }
  private val transition1 = Transition.create(state1, state2, predicate1)

  private def predicate2(event: Event, partialPattern: Array[(Int, Transition, Event)]): Boolean = {
    val createEvent = partialPattern(0)._3
    event.logFile == "NAMENODE" && event.properties("exception") == "LeaseExpiredException" &&
      createEvent.properties("src") == event.properties("file")
  }
  private val transition2 = Transition.create(state2, state3, predicate2)

  override val states: Array[State] = Array(state0, state1, state2, state3, timewindowExceededState)
  override val startState: State = state0
  override val endStates: Array[State] = Array(state3, timewindowExceededState)
  override val transitions: Array[Transition] = Array(transition0, transition1, transition2)
  override val startTransitions: Array[Transition] = transitions.filter(_.from == startState)

  override def aggregate(partialPattern: Array[(Int, Transition, Event)]): ResultType = {
    val createEvent = partialPattern(0)._3
    val deleteEvent = partialPattern(1)._3
    val leaseExpiredEvent = partialPattern(2)._3

    ResultType(
      "Lease Expired Pattern",
      createEvent.properties("src"),
      deleteEvent.properties("src"),
      leaseExpiredEvent.timestamp - createEvent.timestamp,
      leaseExpiredEvent.timestamp - deleteEvent.timestamp
    )
  }
}

object LeaseExpiredExceptionModel {

  case class ResultType(name: String, createdPath: String, deletedPath: String, timegapCreateAndException: Long,
                        timegapDeleteAndException: Long)
}

*/