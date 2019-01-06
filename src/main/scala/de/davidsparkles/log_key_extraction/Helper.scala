package de.davidsparkles.log_key_extraction

object Helper {

  def tokenize(text: String): Array[String] = {
    text.split(" ")
  }

  def doMatch(sequence1: Array[String], sequence2: Array[String]): Boolean = {
    if (sequence1.length != sequence2.length) return false
    val result: Array[Boolean] = new Array[Boolean](sequence1.length)
    for (i <- sequence1.indices) {
      result(i) = sequence1(i) == sequence2(i)
    }
    if (result.count(_ == true) > result.length / 2) true
    else false
  }

  def mergeToLogKey(sequence1: Array[String], sequence2: Array[String]): Array[String] = {
    if (sequence1.length != sequence2.length) return null
    val result = sequence1.clone()
    for (i <- sequence1.indices) {
      result(i) = if (sequence1(i) == sequence2(i)) sequence1(i) else "*"
    }
    result
  }
}
