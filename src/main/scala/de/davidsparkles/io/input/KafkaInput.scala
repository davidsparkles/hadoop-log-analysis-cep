package de.davidsparkles.io.input

import java.util
import java.util.Properties

import com.typesafe.config.Config
import de.davidsparkles.core.io.Input
import de.davidsparkles.io.input.KafkaInput.EventDeserializer
import de.davidsparkles.io
import org.apache.kafka.clients.consumer.{ConsumerRecords, KafkaConsumer}
import org.apache.kafka.common.serialization.{Deserializer, StringDeserializer}
import org.apache.logging.log4j.scala.Logging

import scala.reflect.runtime.universe.TypeTag
import scala.collection.JavaConverters.{asScalaSetConverter, _}

class KafkaInput[Type >: Null](config: Config, asJson: Boolean)(implicit tag: TypeTag[Type])
  extends Input[Type] with Logging {

  private val topic: String = config.getString("topic")
  private val bootstrapServer: String = config.getString("bootstrapServer")
  private val groupId: String = config.getString("groupId")
  private val timeout: Long = config.getDuration("timeout").toMillis

  val properties: Properties = new Properties()
  properties.put("bootstrap.servers", bootstrapServer)
  properties.put("group.id", groupId)

  private val consumer: KafkaConsumer[String, Type] = new KafkaConsumer(
    properties, new StringDeserializer(), new EventDeserializer[Type](asJson)
  )
  consumer.seekToBeginning(consumer.assignment().asScala.toSeq:_*)
  consumer.subscribe(java.util.Arrays.asList(topic))

  override def receive: Iterator[Type] = {
    val records: ConsumerRecords[String, Type] = consumer.poll(timeout)
    records.iterator().asScala.map(record => record.value())
  }

  override def close(): Unit = if (consumer != null) consumer.close()
}

object KafkaInput {

  private class EventDeserializer[Type >: Null](asJson: Boolean)(implicit tag: TypeTag[Type])
    extends Deserializer[Type] {

    private val deserializer = if (asJson) new io.serialization.Deserializer[Type]() else null

    override def configure(configs: util.Map[String, _], isKey: Boolean): Unit = {}
    override def deserialize(topic: String, entries: Array[Byte]): Type =
      deserializer.fromJsonBytes(entries)
    override def close(): Unit = {}
  }
}
