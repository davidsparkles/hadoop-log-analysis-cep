package de.davidsparkles.io.input

import com.typesafe.config.Config
import de.davidsparkles.core.io.Input
import de.davidsparkles.io.serialization.Deserializer
import org.apache.logging.log4j.scala.Logging

import scala.io.Source
import scala.reflect.runtime.universe.TypeTag


class FileReader[Type >: Null](config: Config, asJson: Boolean)(implicit tag: TypeTag[Type]) extends Input[Type] with Logging {

  private val filePath: String = config.getString("filePath")
  private val deserializer = if (asJson) new Deserializer[Type]() else null
  private var hasBeenRead = false

  logger.info(s"Using FileReader with path: $filePath")

  override def receive: Iterator[Type] = {
    val lines: Iterator[String] = if (!hasBeenRead) {
      hasBeenRead = true
      Source.fromFile(filePath).getLines()
    } else Iterator[String]()
    if (deserializer != null) {
      lines.map(line => deserializer.fromJsonString(line))
    } else {
      lines.asInstanceOf[Iterator[Type]]
    }
  }
}

