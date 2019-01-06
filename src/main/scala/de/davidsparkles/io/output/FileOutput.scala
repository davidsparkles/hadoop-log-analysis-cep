package de.davidsparkles.io.output

import java.io.{BufferedWriter, File, FileWriter}

import com.typesafe.config.Config
import de.davidsparkles.core.io.Output
import de.davidsparkles.io.serialization.Serializer
import org.apache.logging.log4j.scala.Logging


class FileOutput[Type >: Null](config: Config, asJson: Boolean) extends Output[Type] with Logging {

  private val filePath: String = config.getString("filePath")
  private val serializationHandler = if (asJson) new Serializer[Type]() else null

  logger.info(s"Using FileOutput with path: $filePath")

  private val file = new File(filePath)
  private val bufferedWriter: BufferedWriter = new BufferedWriter(new FileWriter(file, true))

  override def output(entry: Type): Unit = {

    try {
      val entryString = if (serializationHandler != null) serializationHandler.toJsonString(entry) else entry.toString
      bufferedWriter.write(entryString + "\n")
    } catch {
      case _: Throwable => logger.error(s"FileOutput failed for entry: $entry")
    }
  }

  override def close(): Unit = bufferedWriter.close()
}
