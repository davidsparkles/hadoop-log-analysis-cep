package de.davidsparkles.model.container_memory_usage

import com.typesafe.config.ConfigFactory
import de.davidsparkles.core.finite_state_machine.Event
import de.davidsparkles.io.input.StubInput
import de.davidsparkles.logevents.LogEventWithParameters
import de.davidsparkles.model.OutputTester
import de.davidsparkles.model.container_memory_usage.ContainerMemoryUsageResult.Application
import de.davidsparkles.runner.PatternDetectionRunner
import org.scalatest._

class ContainerMemoryUsageModelSpec extends FlatSpec with Matchers {


  val receiver = new StubInput(TestData.logEvents.asInstanceOf[Array[Event]])

  var actualResult: Application = _

  def testOutput(result: ContainerMemoryUsageResult.Application): Unit = actualResult = result

  private val config = ConfigFactory.load("containerMemoryUsageTransitions.conf").getConfig("transitions")

  val model = new ContainerMemoryUsageModel(config)

  model.setOutput(new OutputTester[model.ResultType](testOutput))

  val runner = new PatternDetectionRunner(receiver)
  runner.addFiniteStateMachineModel(model)

  runner.processEvents(receiver.receive)

  it should "return a result" in {
    actualResult should not equal null
  }

  if (actualResult != null) {

    it should "have the correct application information" in {
      actualResult.applicationId should equal(TestData.expectedResult.applicationId)
      actualResult.applicationStartTimestamp should equal(TestData.expectedResult.applicationStartTimestamp)
      actualResult.applicationEndTimestamp should equal(TestData.expectedResult.applicationEndTimestamp)
      // result.applicationId should equal (TestData.expectedResult.applicationId)
    }
    for (expectedContainer <- TestData.expectedResult.containers) {

      val actualContainer = actualResult.containers.find(_.containerId == expectedContainer.containerId).get
      expectedContainer.containerId should "be contained" in {
        actualContainer should not equal null
      }
      expectedContainer.containerId should "have correct container information" in {
        expectedContainer.containerId should equal(actualContainer.containerId)
        expectedContainer.containerStartTimestamp should equal(actualContainer.containerStartTimestamp)
        expectedContainer.containerEndTimestamp should equal(actualContainer.containerEndTimestamp)
        expectedContainer.memoryObservations should equal(actualContainer.memoryObservations)
      }
    }
  }
}
