package de.davidsparkles.runner

import java.io.{BufferedReader, InputStreamReader}

object MainThreadSleep {

  def tillInfinity(): Unit = while (true) { }

  def tillTimeoutExpires(timeout: Long = 1000): Unit = Thread.sleep(timeout)

  def tillSystemInStop(stopCommand: String = "stop"): Unit = {

    val inputStreamReader = new InputStreamReader(System.in)
    val bufferedReader = new BufferedReader(inputStreamReader)
    var systemInput: String = null
    do {
      systemInput = bufferedReader.readLine
    } while (systemInput != stopCommand)
  }
}
