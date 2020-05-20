package com.hiscat.gmall.realtime.app

import java.time.{Instant, LocalDateTime, ZoneId}
import java.time.format.DateTimeFormatter

import com.alibaba.fastjson.JSON
import com.hiscat.constan.GmallConstants
import com.hiscat.gmall.realtime.bean.StartUpLog
import com.hiscat.gmall.realtime.dao.DauDao
import com.hiscat.gmall.realtime.util.GmallKafkaUtil
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
      .map(e => {
        val log = JSON.parseObject(e.value(), classOf[StartUpLog])
        val now = LocalDateTime.ofInstant(Instant.ofEpochMilli(log.ts), ZoneId.systemDefault())
        log.logDate = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        log.logHour = now.getHour.toString
        log
      })

    DauDao.writeRedis(events)

    ssc.start()
    ssc.awaitTermination()
  }
}
