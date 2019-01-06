package de.davidsparkles.core.agent

import com.typesafe.config.ConfigFactory
import de.davidsparkles.core.finite_state_machine.Event
import de.davidsparkles.logevents.{LogEventWithMessage, LogEventWithParameters}
import de.davidsparkles.parsing.{FeatureExtractor, LogEntryParser}
import org.scalatest.{FlatSpec, Matchers}

class ParameterExtractorSpec extends FlatSpec with Matchers {

  it should "extract the parameters correctly" in {

    val featuresConfig = ConfigFactory.load("parameterExtractionRules.conf").getConfig("agent.features")
    val loggingClass = "core.ThreadHandler"
    val lineNumber = 10
    val sample = "Starting container_01 with 99 threads"
    val featureExtractor = new FeatureExtractor(featuresConfig)
    val result = featureExtractor.extractParameters(loggingClass, lineNumber, sample)

    result.contains("containerId") should equal (true)
    result.contains("numberOfThreads") should equal (true)
    result("containerId") should equal ("container_01")
    result("numberOfThreads") should equal ("99")
  }

  it should "extract the features correctly" in {

    val config = ConfigFactory.load("featureExtractionAgent.conf")
    val sample = "2018-05-13 00:00:00,000 INFO  nodemanager.DefaultContainerExecutor (DefaultContainerExecutor.java:" +
      "deleteAsUser(501)) - Deleting path : /hadoop/yarn/log/application_1526206706116_0010"
    val parser = new LogEntryParser(config)
    val result: LogEventWithMessage = parser.parse(sample).asInstanceOf[LogEventWithMessage]

    result should not be null
    result.timestamp should equal (1526162400000L)
    result.severity should be ("INFO")
    result.logFile should be ("TEST")
    result.server should be ("test.com")
    result.loggingClass should be ("nodemanager.DefaultContainerExecutor")
    result.lineNumber should equal (501)
    result.message should be ("Deleting path : /hadoop/yarn/log/application_1526206706116_0010")
  }

  it should "return null Event if the log cannot be parsed" in {

    val config = ConfigFactory.load("featureExtractionAgent.conf")
    val sample = "2018-05-13 00:00:00,000 INFO  nodemanager.DefaultContainerExecutor (DefaultContainerExecutor.java:" +
      "deleteAsUser:501) - Deleting path : /hadoop/yarn/log/application_1526206706116_0010"
    val parser = new LogEntryParser(config)
    val result: Event = parser.parse(sample)

    result should equal (null)
  }

}
