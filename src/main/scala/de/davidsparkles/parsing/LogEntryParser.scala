package de.davidsparkles.parsing

import java.text.SimpleDateFormat

import com.typesafe.config.Config
import de.davidsparkles.core.agent.Parser
import de.davidsparkles.core.finite_state_machine.Event
import de.davidsparkles.logevents.{LogEventWithMessage, LogEventWithParameters}


class LogEntryParser(config: Config) extends Parser[String, Event] {

  private val featuresConfig = config.getConfig("agent.features")
  private val logFile = featuresConfig.getString("logFile")
  private val server = featuresConfig.getString("server")
  private val applyParameterParsing = featuresConfig.getBoolean("applyParameterParsing")
  private val featureExtractor = new FeatureExtractor(featuresConfig)

  override def parse(entry: String): Event = {
    try {
      val features = featureExtractor.extractMetadataAndMessage(entry)

      val timestampString = getExtractedOrPreconfiguredFeature("timestamp", features)
      val timestamp = getTimestamp(timestampString)
      val severity = getExtractedOrPreconfiguredFeature("severity", features)
      val loggingClass = getExtractedOrPreconfiguredFeature("loggingClass", features)
      val lineNumber = getExtractedOrPreconfiguredFeature("lineNumber", features).toInt
      val message = getExtractedOrPreconfiguredFeature("message", features)
      if (applyParameterParsing) {
        val parameters: Map[String, String] = featureExtractor.extractParameters(loggingClass, lineNumber, message)
        new LogEventWithParameters(timestamp, logFile, server, severity, loggingClass, lineNumber, parameters)
      } else {
        new LogEventWithMessage(timestamp, logFile, server, severity, loggingClass, lineNumber, message)
      }
    } catch {
      case _: Throwable => null
    }
  }

  private def getExtractedOrPreconfiguredFeature(key: String, features: Map[String, String]): String =
    features.getOrElse(key, featuresConfig.getString(key))

  private val timestampFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS")
  private def getTimestamp(timestampString: String): Long = timestampFormat.parse(timestampString).getTime
}
