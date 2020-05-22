package com.hiscat.gmall.realtime.app

import java.time.{Instant, LocalDate, LocalDateTime, ZoneId}
import java.time.format.DateTimeFormatter

import com.alibaba.fastjson.JSON
import com.hiscat.constan.GmallConstants
import com.hiscat.gmall.realtime.bean.StartUpLog
import com.hiscat.gmall.realtime.util.{GmallKafkaUtil, RedisUtil}
import org.apache.hadoop.conf.Configuration
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.{Seconds, StreamingContext}

object DauApp {

  def main(args: Array[String]): Unit = {
    val ssc: StreamingContext = new StreamingContext(
      new SparkConf().setAppName("DauApp").setMaster("local[*]"),
      Seconds(3)
    )


    val events = GmallKafkaUtil
      .getKafkaDStream(GmallConstants.KAFKA_TOPIC_STARTUP, ssc)
      //extract logDate hour
      .map(extractLogDate)
      //filter duplicate user by using redis
      .transform(deduplicateByRedis)
      //filter duplicate user
      .transform(deduplicateByMid)
      .cache()

    //write to hbase
    events.foreachRDD(write2Hbase)

    //write to redis
    events.foreachRDD(write2Redis)
    ssc.start()
    ssc.awaitTermination()
  }


  private def extractLogDate: ConsumerRecord[String, String] => StartUpLog = record => {
    val log = JSON.parseObject(record.value(), classOf[StartUpLog])
    val now = LocalDateTime.ofInstant(Instant.ofEpochMilli(log.ts), ZoneId.systemDefault())
    log.logDate = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    log.logHour = now.getHour.toString
    log
  }

  private def deduplicateByRedis: RDD[StartUpLog] => RDD[StartUpLog] = rdd => {
    val client = RedisUtil.getJedisClient
    val mids = rdd.sparkContext.broadcast(client.smembers(s"Dau:${LocalDate.now.toString}"))
    client.close()
    rdd.filter(e => !mids.value.contains(e.mid))
  }

  private def deduplicateByMid: RDD[StartUpLog] => RDD[StartUpLog] =
    _.groupBy(e => (e.mid, e.logDate))
      .map {
        case (_, logs) => logs.minBy(_.ts)
      }

  private def write2Hbase: RDD[StartUpLog] => Unit = {
    rdd => {
      import org.apache.phoenix.spark._
      rdd.saveToPhoenix("GMALL2020_DAU",
        classOf[StartUpLog].getDeclaredFields.map(_.getName.toUpperCase),
        new Configuration,
        Some("hadoop102,hadoop103,hadoop104:2181"))

    }
  }

  private def write2Redis: RDD[StartUpLog] => Unit = {
    rdd => {
      rdd.foreachPartition(it => {
        val client = RedisUtil.getJedisClient

        it.foreach(e => {
          client.sadd(s"Dau:${e.logDate}", e.mid)
        })

        client.close()
      })
    }
  }

}
