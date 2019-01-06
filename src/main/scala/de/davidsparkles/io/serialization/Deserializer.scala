package de.davidsparkles.io.serialization

import com.fasterxml.jackson.core.`type`.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import de.davidsparkles.logevents.{LogEventWithMessage, LogEventWithParameters}
import org.apache.logging.log4j.scala.Logging

import scala.reflect.runtime.universe._

class Deserializer[Type >: Null](implicit tag: TypeTag[Type]) extends Logging {

  private val objectMapper = new ObjectMapper()
  objectMapper.registerModule(DefaultScalaModule)

  def fromJsonString(jsonString: String): Type = {

    try {
      if (tag == typeTag[LogEventWithParameters])
        objectMapper.readValue(jsonString, new TypeReference[LogEventWithParameters] {}).asInstanceOf[Type]
      else if (tag == typeTag[LogEventWithMessage])
        objectMapper.readValue(jsonString, new TypeReference[LogEventWithMessage] {}).asInstanceOf[Type]
      else throw new Exception(s"Type tag is not known and thus the json string cannot be parsed into this type: $tag")
    } catch {
      case exception: Throwable => logger.error(s"Exception for jsonstring: $jsonString \n " +
        s"Exception: ${exception.printStackTrace()}")
        null
    }
  }

  def fromJsonBytes(data: Array[Byte]): Type = fromJsonString(data.toString)
}
