package de.davidsparkles.io.serialization

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility
import com.fasterxml.jackson.annotation.PropertyAccessor
import com.fasterxml.jackson.core.`type`.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import org.apache.logging.log4j.scala.Logging

class Serializer[Type >: Null] extends Logging {

  def toJsonString(item: Type): String = {

    val objectMapper = new ObjectMapper()
    objectMapper.registerModule(DefaultScalaModule)
    objectMapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY)
    objectMapper.writeValueAsString(item)
  }

  def toJsonBytes(item: Type): Array[Byte] = toJsonString(item).getBytes()
}
