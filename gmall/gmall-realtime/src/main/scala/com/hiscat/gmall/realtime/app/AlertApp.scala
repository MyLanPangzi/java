package com.hiscat.gmall.realtime.app

import java.time.{Instant, LocalDate, LocalDateTime, LocalTime, ZoneId}
import java.time.format.DateTimeFormatter
import java.util

import com.alibaba.fastjson.JSON
import com.hiscat.constan.GmallConstants
import com.hiscat.gmall.realtime.bean.{CouponAlertInfo, EventLog}
import com.hiscat.gmall.realtime.util.{GmallEsUtil, GmallKafkaUtil}
import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Minutes, Seconds, StreamingContext}

import scala.collection.immutable.HashSet
import scala.util.control.Breaks

object AlertApp {


  def main(args: Array[String]): Unit = {
    val ssc: StreamingContext = new StreamingContext(
      new SparkConf().setAppName("DauApp").setMaster("local[*]"),
      Seconds(5)
    )
    //event log -> alter
    GmallKafkaUtil
      .getKafkaDStream(GmallConstants.KAFKA_TOPIC_EVENT, ssc)
      .map(e => {
        val log = JSON.parseObject(e.value(), classOf[EventLog])
        val now = LocalDateTime.ofInstant(Instant.ofEpochMilli(log.ts), ZoneId.systemDefault())
        log.logDate = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        log.logHour = now.getHour.toString
        //        println(log)
        log
      })
      .map(event => (event.mid, event))
      .groupByKeyAndWindow(Seconds(30), Seconds(10))
      .map {
        case (mid, logs) =>
          val uids: util.Set[String] = new util.HashSet[String]
          val items = new util.HashSet[String]
          val events = new util.ArrayList[String]
          Breaks.breakable {
            for (log <- logs) {
              if (log.evid == "clickItem") {
                Breaks.break()
              } else if (log.evid == "coupon") {
                uids.add(log.uid)
                items.add(log.itemid)
              }
              events.add(log.evid)
            }
          }
          (uids.size() > 3, CouponAlertInfo(mid, uids, items, events, System.currentTimeMillis()))
      }
      .filter(_._1)
      .map(_._2)
      .map(log => {
        (LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")), log)
      })
      .foreachRDD(_.foreachPartition(it =>
        GmallEsUtil.insertBulk(GmallConstants.ES_ALERT_INDEX + LocalDate.now().toString, it.toSeq)
      ))

    ssc.start()
    ssc.awaitTermination()
  }

}
