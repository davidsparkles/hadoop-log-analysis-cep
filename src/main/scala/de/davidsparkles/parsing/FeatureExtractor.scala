package de.davidsparkles.parsing

import com.typesafe.config.{Config, ConfigException}
import de.davidsparkles.parsing.FeatureExtractor.{ExtractionRule, ExtractionException, UnkownLoggingClassAndLineNumber}
import org.apache.logging.log4j.scala.Logging

import scala.collection.mutable.ArrayBuffer
import scala.util.matching.Regex


class FeatureExtractor(config: Config) extends Logging {

  private val rules: Array[FeatureExtractor.ExtractionRule] = getRules()

  def extractParameters(loggingClass: String, lineNumber: Int, message: String): Map[String, String] = {

    val rule = rules.find(rule => rule.loggingClass == loggingClass && rule.lineNumber == lineNumber).orNull
    if (rule == null) throw new UnkownLoggingClassAndLineNumber()
    extract(message, rule.regex, rule.keys)
  }

  def extractMetadataAndMessage(log: String): Map[String, String] = {

    val keys = getKeys()
    val regex = config.getString("regex")
    extract(log, regex, keys)
  }

  private def extract(string: String, regexString: String, keys: Array[String]): Map[String, String] = {

    try {
      val regex = new Regex(regexString, keys:_*)
      val firstMatch = regex.findFirstMatchIn(string)
      if (firstMatch.isEmpty || firstMatch.get.groupCount != keys.length) throw new Exception()
      val result: ArrayBuffer[(String, String)] = ArrayBuffer()
      for (key <- keys) result += ((key, firstMatch.get.group(key)))
      result.toMap
    } catch {
      case _: Throwable => throw new ExtractionException()
    }
  }

  def extractAuditLogParameters(message: String): Map[String, String] = {
    message.split("\\t").map(_.split("=")).map(parameter => (parameter(0), parameter(1))).toMap
  }

  private def getRules(extractionConfig: Config = config): Array[ExtractionRule] = {

    try {
      val extractionRuleConfigList = extractionConfig.getConfigList("parameterExtractionRules")
      val rules: ArrayBuffer[ExtractionRule] = ArrayBuffer()
      for (i <- 0 until extractionRuleConfigList.size()) {
        val extractionRuleConfig = extractionRuleConfigList.get(i)
        val loggingClass = extractionRuleConfig.getString("loggingClass")
        val lineNumber = extractionRuleConfig.getInt("lineNumber")
        val regex = extractionRuleConfig.getString("regex")
        val keys = getKeys(extractionRuleConfig)
        rules += ExtractionRule(loggingClass, lineNumber, regex, keys)
      }
      rules.toArray
    } catch {
      case _: ConfigException => logger.warn("No parameterExtractionRules config property was found.")
        Array()
    }
  }

  private def getKeys(extractionConfig: Config = config): Array[String] = {

    val keys = ArrayBuffer[String]()
    val keysConfig = extractionConfig.getStringList("keys")
    for (j <- 0 until keysConfig.size()) keys += keysConfig.get(j)
    keys.toArray
  }
}

object FeatureExtractor {

  private case class ExtractionRule(loggingClass: String, lineNumber: Int, regex: String, keys: Array[String])

  class ExtractionException extends Throwable

  class UnkownLoggingClassAndLineNumber extends Throwable
}
