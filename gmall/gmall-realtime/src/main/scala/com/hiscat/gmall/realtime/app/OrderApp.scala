package com.hiscat.gmall.realtime.app

import com.alibaba.fastjson.JSON
import com.hiscat.constan.GmallConstants
import com.hiscat.gmall.realtime.bean.OrderInfo
import com.hiscat.gmall.realtime.util.GmallKafkaUtil
import org.apache.hadoop.conf.Configuration
import org.apache.phoenix.spark._
import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}

object OrderApp {
  def main(args: Array[String]): Unit = {
    val ssc: StreamingContext = new StreamingContext(
      new SparkConf().setAppName("OrderApp").setMaster("local[*]"),
      Seconds(3)
    )
    GmallKafkaUtil.getKafkaDStream(GmallConstants.KAFKA_TOPIC_ORDER_INFO, ssc)
      .map(record => {
        val order = JSON.parseObject(record.value(), classOf[OrderInfo])
        val split = order.create_time.split(" ")
        order.create_date = split(0)
        order.create_hour = split(1).split(":")(0)
        order.consignee_tel = order.consignee_tel.splitAt(3)._1 + "********"
        order
      })
      .foreachRDD(
        _.saveToPhoenix("GMALL2020_ORDER_INFO",
          classOf[OrderInfo].getDeclaredFields.map(_.getName.toUpperCase),
          new Configuration,
          Some("hadoop102,hadoop103,hadoop104:2181"))
      )

    ssc.start()
    ssc.awaitTermination()
  }

}
