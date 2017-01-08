package org.caravan

import org.apache.kafka.clients.consumer.ConsumerRecord

import org.apache.spark._
import org.apache.spark.SparkContext
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.streaming.kafka010._
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.apache.spark.streaming.StreamingContext

import org.apache.log4j._

object Holder extends Serializable {
  @transient lazy val log = Logger.getLogger(getClass.getName)
}

object Consumer {
  def main(args : Array[String]) {
    val logger = Logger.getLogger(getClass().getName())

    if (args.length != 3) {
      println (s"""\n\nERROR:\nUSAGE:\n java -Dlog4j.configuration="file:<full_path_to_log4j.xml>" -cp target\\uber-kafkaexample-1.0-SNAPSHOT.jar org.caravan.Consumer <topicName> <kafka_postgres_host_ip> <output_dir> \n """)
    }

    val topic = args(0)
    val kafka_and_postgres_ip = args(1)
    val output_dir = args(2)
    logger.info(s"""Arguments provided: \n\t topic: ${topic} \n\t kafka_and_postgres_ip: ${kafka_and_postgres_ip} \n\t output_dir : ${output_dir}""")

    val db_con_str = s"""jdbc:postgresql://${kafka_and_postgres_ip}:5432/testdb?user=bdcuser&password=bdcuser"""

    logger.info("At the beginning of the Spark Streaming program ... ")
    val conf = new SparkConf().
      setMaster("local[4]").
      setAppName("KafkaConsumerExample")

    val sparkContext = new SparkContext(conf)
    val duration = org.apache.spark.streaming.Seconds(10)
    val streamingContext = new StreamingContext(sparkContext, duration)

    val kafkaParams = Map[String, Object](
      "bootstrap.servers" -> s"""${kafka_and_postgres_ip}:9092""",
      "key.deserializer" -> classOf[StringDeserializer],
      "value.deserializer" -> classOf[StringDeserializer],
      "group.id" -> "consumer",
      "auto.offset.reset" -> "earliest",
      "enable.auto.commit" -> (false: java.lang.Boolean)
    )

    val topics = Array("test-topic")
    val stream = KafkaUtils.createDirectStream[String, String](
      streamingContext,
      PreferConsistent,
      Subscribe[String, String](topics, kafkaParams)
    )

    stream.map(record => (record.key, record.value))

    stream.foreachRDD { rdd =>

      val logger1 = Logger.getLogger(getClass().getName())
      val offsetRanges = rdd.asInstanceOf[HasOffsetRanges].offsetRanges

      rdd.foreachPartition{iter =>
        val o: OffsetRange = offsetRanges(TaskContext.get.partitionId)
        println(s"${o.topic} ${o.partition} ${o.fromOffset} ${o.untilOffset}")
        import java.io._

        import java.sql.{Connection, DriverManager, ResultSet}
        classOf[org.postgresql.Driver]
        val conn = DriverManager.getConnection(db_con_str)
        try {
          val stm = conn.createStatement()
          //val datawriter2 = new FileWriter(s"C:\\JavaDev\\scalaWorkspace\\KafkaExample2\\output\\dataFile_PG_DMLStatements${o.partition}.txt", true)
          val datawriter2 = new FileWriter(s"${output_dir}dataFile_PG_DMLStatements${o.partition}.txt", true)

          iter.foreach { x =>
           val insertStr = s""" Insert Into "MyDataTable" values ('${x.key()}', '${x.value()}', ${o.partition}) \n"""
           val rs = stm.executeUpdate(insertStr)
            //conn.commit()   //No commit is needed as the the default is autocommit
            datawriter2.write(insertStr)
          }
          datawriter2.flush()
          datawriter2.close()
        } finally {
          conn.close()
        }
      }

      stream.asInstanceOf[CanCommitOffsets].commitAsync(offsetRanges)
    }

    streamingContext.start()
    streamingContext.awaitTermination()
    logger.info("Exiting the Streaming process ...")
  }
}
