package de.davidsparkles.log_key_extraction

import org.scalatest._

class LogKeyExtractorSpec extends FlatSpec with Matchers {

  val testData: Array[(String, String, Array[String])] = Array( //[(String, Array[String])](4)
    ("The temperature is 6 C", "The temperature is * C", Array("6")),
    ("David is my name", "* is my name", Array("David")),
    ("Dominik is my name", "* is my name", Array("Dominik")),
    ("The temperature is 14 C", "The temperature is * C", Array("14"))

  )

  val logKeys: Array[String] = Array(
    "The temperature is * C",
    "* is my name"
  )

  it should "extract log keys" in {
    val data = testData.map(entry => Helper.tokenize(entry._1))
    val logKeysActual = LogKeyExtractor.extractLogKeys(data).map(_.mkString(" "))
    logKeysActual should not be null
    logKeysActual should be (logKeys)
  }

  it should "extract parameters given the log keys" in {
    for (testEntry <- testData) {
      val entry = Helper.tokenize(testEntry._1)
      val lks = logKeys.map(Helper.tokenize(_))
      val (logKey, parameters) = LogKeyExtractor.mapToLogKeyAndParameters(entry, lks)
      val lk = logKey.mkString(" ")
      lk should not be null
      lk should be (testEntry._2)
      parameters should not be null
      parameters should be (testEntry._3)
    }
  }

  /**
  it should "throw NoSuchElementException if an empty stack is popped" in {
    // val emptyStack = new Stack[Int]
    a [NoSuchElementException] should be thrownBy {
      // emptyStack.pop()
    }
  }
  **/
}
