package de.davidsparkles.core.finite_state_machine

import com.typesafe.config.Config
import de.davidsparkles.core.io.Output
import de.davidsparkles.io.serialization.Serializer
import javax.swing.event.HyperlinkEvent.EventType
import org.apache.logging.log4j.scala.Logging

import scala.collection.mutable.ArrayBuffer


abstract class FiniteStateMachineModel extends Logging {

  type EventType >: Null <: Event
  type ResultType >: Null <: Any
  val transitionConfig: Config

  val states: Array[State]
  val startState: State
  val endStates: Array[State]
  val transitions: Array[Transition[EventType]]
  val startTransitions: Array[Transition[EventType]]
  val timewindow: Long
  val timewindowExceededState: State
  var output: Output[ResultType]

  val serializationHandler = new Serializer[Any]()

  val finiteStateMachines: ArrayBuffer[FiniteStateMachine[EventType]] = ArrayBuffer()

  def setOutput(output: Output[ResultType]): Unit = this.output = output

  def processEvent(event: EventType): Unit = {

    createMachine(event)
    finiteStateMachines.foreach(finiteStateMachine => {
      if (!checkIfTimeWindowIsExceeded(event, finiteStateMachine)) {
        val (nextState, transition) = inferNextState(event, finiteStateMachine)
        if (nextState != null) {
          finiteStateMachine.state = nextState
          finiteStateMachine.partialPattern += MatchedEvent[EventType](event, transition)
        }
      } else {
        finiteStateMachine.state = timewindowExceededState
      }
    })
    removeFinishedMachines()
  }

  protected def createMachine(event: EventType): Unit

  protected def addFiniteStateMachine(finiteStateMachine: FiniteStateMachine[EventType]): Unit = {

    this.finiteStateMachines += finiteStateMachine
  }

  protected def isFinished(state: State): Boolean = this.endStates.contains(state)

  protected def aggregate(finiteStateMachine: FiniteStateMachine[EventType]): ResultType

  private def removeFinishedMachines(): Unit = {
    val finishedMachines = finiteStateMachines.filter(finiteStateMachine => isFinished(finiteStateMachine.state))
      .map(finiteStateMachine => {
        val result = aggregate(finiteStateMachine)
        if (result != null) logger.info(s"Found result: ${serializationHandler.toJsonString(result)}")
        else logger.info("Found result with value 'null'")
        output.output(result)
        finiteStateMachine
      })
    finiteStateMachines --= finishedMachines
  }

  private def checkIfTimeWindowIsExceeded(event: EventType, finiteStateMachine: FiniteStateMachine[EventType]): Boolean =
    finiteStateMachine.partialPattern.nonEmpty &&
    event.timestamp - finiteStateMachine.partialPattern.head.event.timestamp > timewindow

  private def inferNextState(event: EventType,
                             finiteStateMachine: FiniteStateMachine[EventType]): (State, Transition[EventType]) = {

    val transitionOption = transitions.find(transition => transition.from == finiteStateMachine.state &&
      transition.evaluate(event, finiteStateMachine)
    )
    if (transitionOption.isEmpty) return (null, null)
    val transition = transitionOption.get
    val nextState = transition.to
    (nextState, transition)
  }
}