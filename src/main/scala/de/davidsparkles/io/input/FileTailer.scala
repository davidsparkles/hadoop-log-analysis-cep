package de.davidsparkles.io.input

import java.io.File

import com.typesafe.config.Config
import de.davidsparkles.core.io.Input
import de.davidsparkles.io.input.FileTailer.FileTailerListener
import de.davidsparkles.io.input.tailer.{Tailer, TailerListenerAdapter}

import scala.collection.mutable.ListBuffer


class FileTailer(config: Config) extends Input[String] {

  private val filePath = config.getString("filePath")
  private val startFromTheEnd = config.getBoolean("startFromTheEnd")

  private var listener: FileTailerListener = _
  private var tailer: Tailer = _

  override def receive: Iterator[String] = {
    if (tailer == null) {
      val file = new File(filePath)
      listener = new FileTailerListener()
      tailer = Tailer.create(file, listener, 1, startFromTheEnd)
    }
    listener.getLogEntries
  }

  override def close(): Unit = {
    if (tailer != null) tailer.stop()
  }
}

object FileTailer {

  private class FileTailerListener extends TailerListenerAdapter {

    private val lines: ListBuffer[String] = ListBuffer()

    def getLogEntries: Iterator[String] = {
      val iterator = lines.toIterator
      lines.clear()
      iterator
    }

    override def handle(line: String): Unit = {
      lines += line
    }
  }
}
