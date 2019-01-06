package de.davidsparkles.io.output

import java.util
import java.util.Properties

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility
import com.fasterxml.jackson.annotation.PropertyAccessor
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.typesafe.config.Config
import de.davidsparkles.core.io.Output
import de.davidsparkles.io.output.KafkaOutput.EntrySerializer
import de.davidsparkles.io
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import org.apache.kafka.common.serialization.{Serializer, StringSerializer}
import org.apache.logging.log4j.scala.Logging

class KafkaOutput[Type >: Null](config: Config) extends Output[Type] with Logging {

  private val topic = config.getString("topic")
  private val bootstrapServer = config.getString("bootstrapServer")
  logger.info(s"Use KafkaProducer as ouput with topic $topic and bootstrapServer $bootstrapServer")

  if (topic == null || topic == "" || bootstrapServer == null || bootstrapServer == "")
    logger.error(s"Bad config: topic = $topic , bootstrapServer = $bootstrapServer")

  private val properties = new Properties()
  properties.put("bootstrap.servers", bootstrapServer)

  private val producer: KafkaProducer[String, Type] = new KafkaProducer(
    properties,
    new StringSerializer(),
    new EntrySerializer[Type]()
  )

  override def output(entry: Type): Unit = {
    producer.send(new ProducerRecord[String, Type](topic, "", entry))
  }

  override def close(): Unit = if (producer != null) producer.close()
}

object KafkaOutput {

  private class EntrySerializer[Type >: Null] extends Serializer[Type] {

    private val serializationHandler = new io.serialization.Serializer[Type]()

    override def configure(configs: util.Map[String, _], isKey: Boolean): Unit = {}

    override def serialize(topic: String, entry: Type): Array[Byte] = {
      val objectMapper = new ObjectMapper()
      objectMapper.registerModule(DefaultScalaModule)
      objectMapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY)
      try {
        val s = objectMapper.writeValueAsString(entry)
        return s.getBytes()
      } catch {
        case _: Throwable => println("Serialization Exception")
      }
      null
    }

    override def close(): Unit = {}
  }
}
