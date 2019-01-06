package de.davidsparkles.io.output

import de.davidsparkles.core.io.Output
import de.davidsparkles.io.serialization.Serializer
import org.apache.logging.log4j.scala.Logging

class ConsoleOutput[Type >: Null](asJson: Boolean) extends Output[Type] with Logging {

  private val serializationHandler = if (asJson) new Serializer[Type]() else null

  override def output(entry: Type): Unit = {
    try {
      val entryString = if(serializationHandler != null) serializationHandler.toJsonString(entry) else entry.toString
      println(entryString)
    } catch {
      case _: Throwable => logger.error(s"ConsoleOutput failed for entry: $entry")
    }
  }
}
