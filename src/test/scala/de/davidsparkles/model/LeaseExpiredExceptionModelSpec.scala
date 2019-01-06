package de.davidsparkles.model
/*
import de.davidsparkles.core.CEPRunner
import de.davidsparkles.core.finite_state_machine.Event
import org.scalatest._

class LeaseExpiredExceptionModelSpec extends FlatSpec with Matchers {

  val logEvents: Array[Event] = Array(
    Event(timestamp = 0, logFile = "HDFS-AUDIT", properties = Map("src" -> "/folder1/file1", "cmd" -> "create")),
    Event(timestamp = 2, logFile = "HDFS-AUDIT", properties = Map("src" -> "/folder1/file2", "cmd" -> "create")),
    Event(timestamp = 4, logFile = "HDFS-AUDIT", properties = Map("src" -> "/folder1/file3", "cmd" -> "create")),
    Event(timestamp = 5, logFile = "HDFS-AUDIT", properties = Map("src" -> "/folder1", "cmd" -> "delete")),
    Event(timestamp = 7, logFile = "NAMENODE", properties = Map("file" -> "/folder1/file1", "exception" -> "LeaseExpiredException")),
    Event(timestamp = 12, logFile = "HDFS-AUDIT", properties = Map("src" -> "/folder2", "cmd" -> "delete"))
  )

  val expectedResult = LeaseExpiredExceptionModel.ResultType("Lease Expired Pattern", "/folder1/file1", "/folder1", 7, 2)

  val receiver = new FakeEventReceiver(logEvents)

  def testOutput(result: Any): Unit = {
    it should "reveal the expected complex event in the log events" in {
      result should equal (expectedResult)
    }
  }

  val output = new OutputTester(testOutput)

  val model = new LeaseExpiredExceptionModel(output)

  val runner = new CEPRunner(receiver)
  runner.addFiniteStateMachineModel(model)

  runner.runOnce()
}
*/