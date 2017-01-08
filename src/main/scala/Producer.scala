package org.caravan

import java.util.{Date, Properties}
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import scala.util.Random

object Producer extends App {
  val num_of_events = args(0).toInt
  val topic = args(1)
  val brokers = args(2)
  val rnd = new Random()
  val props = new Properties()
  props.put("bootstrap.servers", brokers)
  props.put("client.id", "ProducerExample")
  props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
  props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")

  val producer = new KafkaProducer[String, String](props)
  val t = System.currentTimeMillis()
  for (nEvents <- Range(0, num_of_events)) {
    val runtime = new Date().getTime()
    val key = "key_" + nEvents + "_" + rnd.nextInt(255)
    val msg = runtime + "," + nEvents + ",some_message," + key
    val data = new ProducerRecord[String, String](topic, key, msg)

    producer.send(data)
  }

  System.out.println("sent per second: " + num_of_events * 1000 / (System.currentTimeMillis() - t))
  producer.close()
}

