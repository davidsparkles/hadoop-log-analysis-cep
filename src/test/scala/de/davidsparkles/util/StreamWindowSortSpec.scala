package de.davidsparkles.util

import com.typesafe.config.ConfigFactory
import de.davidsparkles.core.finite_state_machine.Event
import org.scalatest.{FlatSpec, Matchers}


class StreamWindowSortSpec extends FlatSpec with Matchers {

  private val config = ConfigFactory.load("streamWindowSort.conf").getConfig("streamWindowSort")

  val testDataShort: Array[Event] = Array(
    new Event(3), new Event(timestamp = 4), new Event(timestamp = 1), new Event(timestamp = 2), new Event()
  )

  val testDataLong: Array[Event] = Array(
    new Event(timestamp = 9), new Event(timestamp = 4), new Event(timestamp = 7), new Event(timestamp = 6), new Event(timestamp = 5),
    new Event(timestamp = 3), new Event(timestamp = 8), new Event(timestamp = 1), new Event(timestamp = 2), new Event(timestamp = 10),
    new Event()
  )

  it should "reserve the events in the buffer if there is space" in {
    val streamWindowSort = new StreamWindowSort[Event](config)
    streamWindowSort.getNextEvent(testDataShort(0)) should equal (null)
  }

  it should "return the events ordered" in {
    val streamWindowSort = new StreamWindowSort[Event](config)
    for (event <- testDataShort) {
      val nextEvent = streamWindowSort.getNextEvent(event)
      nextEvent should equal (null)
    }
    val previousTimestamp: Long = -1
    while(streamWindowSort.nonEmpty) {
      val event = streamWindowSort.getNextEvent
      event should not equal null
      event.timestamp should be > previousTimestamp
    }
  }

  it should "be limited to the max buffer size" in {
    val streamWindowSort = new StreamWindowSort[Event](config)
    val expectedResults = Array(null, null, null, null, null, 3, 4, 1, 2, 5, 0)
    for (i <- testDataLong.indices) {
      val expected = expectedResults(i)
      val event = testDataLong(i)
      val nextEvent = streamWindowSort.getNextEvent(event)
      if (expected == null) nextEvent should equal (null)
      else nextEvent.timestamp should equal (expected)
    }
  }
}
