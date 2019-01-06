package de.davidsparkles.log_key_extraction

import scala.collection.mutable.ArrayBuffer

object LogKeyExtractor {

  def extractLogKeys(logTexts: Array[Array[String]]): Array[Array[String]] = {
    var logKeys = Array[Array[String]]()
    for (logText <- logTexts) {
      logKeys = getNewLogKeys(logText, logKeys)
    }
    logKeys
  }

  def mapToLogKeyAndParameters(logText: Array[String], logKeys: Array[Array[String]]): (Array[String], Array[String]) = {
    var logKeyMatching: Array[String] = null
    for (logKey <- logKeys.filter(_.length == logText.length)) {
      var isMatch = true
      for (i <- logText.indices) {
        if (logKey(i) != "*" && logKey(i) != logText(i)) isMatch = false
      }
      if (isMatch) logKeyMatching = logKey
    }
    val parameters = ArrayBuffer[String]()
    for (i <- logKeyMatching.indices) {
      if (logKeyMatching(i) == "*") parameters += logText(i)
    }
    (logKeyMatching, parameters.toArray)
  }

  private def getNewLogKeys(logText: Array[String], logKeys: Array[Array[String]]): Array[Array[String]] = {
    val result = ArrayBuffer[Array[String]]()
    var covered = false
    for (logKey <- logKeys) {
      if (Helper.doMatch(logText, logKey)) {
        val logKeyMerged = Helper.mergeToLogKey(logText, logKey)
        covered = true
        result += logKeyMerged
      } else {
        result += logKey
      }
    }
    if (!covered) result += logText
    result.toArray
  }

  private def output(texts: Array[Array[String]]): Unit = {
    texts.foreach(output)
  }

  private def output(text: Array[String]): Unit = {
    println(text.mkString(" "))
  }
}
