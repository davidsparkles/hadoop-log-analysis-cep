package de.davidsparkles

// import de.davidsparkles.io.event_sender.EventSerializer
// import de.davidsparkles.runner.CEPRunner
// import de.davidsparkles.runtime_test.TestDataGenerator

object RuntimeTestMain extends App {

  override def main(args: Array[String]): Unit = {

/**
    val runner = new CEPRunner()
    CEPRunner.addFiniteStateMachineModel(new LeaseExpiredExceptionModel())
    CEPRunner.addFiniteStateMachineModel(new CreateCountModel(10, 1000))

    val output = new ConsoleOutput()

    val t0TestData = System.nanoTime()
    val events = TestDataGenerator.getTestData(1000, 10) // 10000 events ~ 1,4 MB
    // var events = LeaseExpiredException.events
    val t1TestData = System.nanoTime()

    // val eventSerializer =
    val datavolume = events.map(eventSerializer.serialize("", _).length).sum
    val t2TestData = System.nanoTime()

    println(s"Time to generate test data: ${(t1TestData - t0TestData) / Math.pow(10, 9)} seconds")
    println(s"Time to analyze test data: ${(t2TestData - t1TestData) / Math.pow(10, 9)} seconds")
    println(s"Datavolume: $datavolume Bytes")
    println(s"Number of events: ${events.length}")

    val iterations = 10
    var timeSum: Long = 0
    for (i <- 1 to iterations) {
      // println(s"--- $i ---")
      val t0 = System.nanoTime()
      CEPRunner.runOnce(() => events, output)
      val t1 = System.nanoTime()
      CEPRunner.reset()
      timeSum += (t1.toLong - t0.toLong)
    }
    println(s"Number of iterations: $iterations")
    println(s"Average CEP runtime: ${timeSum / iterations / Math.pow(10, 9)} seconds")
    println(s"Total CEP runtime: ${timeSum / Math.pow(10, 9)} seconds")

    println(s"Number of fsms: ${CEPRunner.finiteStateMachineModels.map(_.finiteStateMachines.size).sum}")
  **/
  }

}
