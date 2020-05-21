package com.hiscat.gmall.realtime.app

import java.time.{Instant, LocalDate, LocalDateTime, ZoneId}
import java.time.format.DateTimeFormatter

import com.alibaba.fastjson.JSON
import com.hiscat.constan.GmallConstants
import com.hiscat.gmall.realtime.bean.StartUpLog
import com.hiscat.gmall.realtime.util.{GmallKafkaUtil, RedisUtil}
import org.apache.hadoop.conf.Configuration
import org.apache.spark.SparkConf
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
      .map(e => {
        val log = JSON.parseObject(e.value(), classOf[StartUpLog])
        val now = LocalDateTime.ofInstant(Instant.ofEpochMilli(log.ts), ZoneId.systemDefault())
        log.logDate = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        log.logHour = now.getHour.toString
        log
      })
      //filter duplicate user by using redis
      .transform((rdd, t) => {
        val client = RedisUtil.getJedisClient
        val mids = rdd.sparkContext.broadcast(client.smembers(s"Dau:${LocalDate.now.toString}"))
        client.close()
        rdd.filter(e => !mids.value.contains(e.mid))
      })
      //filter duplicate user
      .transform(_.groupBy(e => (e.mid, e.logDate)).map {
        case (_, logs) => logs.toList.sortBy(_.ts).take(1)
      }.flatMap(e => e))
      .cache()

    //write to hbase
    events.foreachRDD(rdd => {
      import org.apache.phoenix.spark._
      rdd.saveToPhoenix("GMALL2020_DAU",
        Seq("MID", "UID", "APPID", "AREA", "OS", "CH", "TYPE", "VS", "LOGDATE", "LOGHOUR", "TS"),
        new Configuration,
        Some("hadoop102,hadoop103,hadoop104:2181"))

    })

    //write to redis
    events.foreachRDD(rdd => {
      rdd.foreachPartition(it => {
        val client = RedisUtil.getJedisClient

        it.foreach(e => {
          client.sadd(s"Dau:${e.logDate}", e.mid)
        })

        client.close()
      })
    })
    ssc.start()
    ssc.awaitTermination()
  }
}
