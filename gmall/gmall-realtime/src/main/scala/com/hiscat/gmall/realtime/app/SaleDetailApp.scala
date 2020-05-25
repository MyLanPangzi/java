package com.hiscat.gmall.realtime.app

import java.time.LocalDate

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSON.parseObject
import com.alibaba.fastjson.serializer.SerializerFeature
import com.hiscat.constan.GmallConstants
import com.hiscat.gmall.realtime.app.SaleDetailApp.OrderDetailMatch.OrderDetailMatch
import com.hiscat.gmall.realtime.bean.{OrderDetail, OrderInfo, SaleDetail, UserInfo}
import com.hiscat.gmall.realtime.util.{GmallEsUtil, GmallKafkaUtil, RedisUtil}
import org.apache.hadoop.hbase.protobuf.generated.HBaseProtos.TimeUnit
import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}
import redis.clients.jedis.Jedis

object SaleDetailApp {

  def main(args: Array[String]): Unit = {
    val ssc: StreamingContext = new StreamingContext(
      new SparkConf().setAppName("SaleDetailApp").setMaster("local[*]"),
      Seconds(3)
    )
    GmallKafkaUtil.getKafkaDStream(GmallConstants.KAFKA_TOPIC_USER_INFO, ssc)
      .foreachRDD(_.foreachPartition(it => {
        val client = RedisUtil.getJedisClient
        it.foreach(record => {
          val user = parseObject(record.value(), classOf[UserInfo])
          client.set(s"user:${user.id}", record.value())
        })
        client.close()
      }))
    val orderDStream = GmallKafkaUtil.getKafkaDStream(GmallConstants.KAFKA_TOPIC_ORDER_INFO, ssc)
      .map(record => {
        val order = parseObject(record.value(), classOf[OrderInfo])
        val split = order.create_time.split(" ")
        order.create_date = split(0)
        order.create_hour = split(1).split(":")(0)
        order.consignee_tel = order.consignee_tel.splitAt(3)._1 + "********"
        (order.id, order)
      })
    val detailDStream = GmallKafkaUtil.getKafkaDStream(GmallConstants.KAFKA_TOPIC_ORDER_DETAIL, ssc)
      .map(record => parseObject(record.value(), classOf[OrderDetail]))
      .map(o => (o.order_id, o))

    orderDStream
      .fullOuterJoin(detailDStream)
      .mapPartitions(it => {
        val client = RedisUtil.getJedisClient
        val sales = it.map(e => (getKey(e._2), e._2))
          .toIterable
          .groupBy(_._1)
          .flatMap {
            case (OrderDetailMatch.ORDER_DETAIL, orderDetails) => ordersAndDetails2SaleDetails(client, orderDetails)
            case (OrderDetailMatch.ORDER_NULL, orders) => orders2SaleDetails(client, orders)
            case (OrderDetailMatch.NULL_DETAIL, details) => details2SaleDetails(client, details)
          }
        client.close()
        sales.toIterator
      })
      .mapPartitions(it => {
        val client = RedisUtil.getJedisClient
        val result = it.map(s => {
          s.mergeUserInfo(JSON.parseObject(client.get(s"user:${s.user_id}"), classOf[UserInfo]))
          s
        })
        client.close()
        result
      })
      .foreachRDD(_.foreachPartition(it => {
        GmallEsUtil.insertBulk(
          GmallConstants.ES_INDEX_SALE_DETAIL + "_" + LocalDate.now.toString,
          it.map(s => (s.order_detail_id, s)).toList
        )
      }))

    ssc.start()
    ssc.awaitTermination()
  }

  object OrderDetailMatch extends Enumeration {
    type OrderDetailMatch = Value
    val ORDER_DETAIL, ORDER_NULL, NULL_DETAIL = Value
  }

  def getKey(sale: (Option[OrderInfo], Option[OrderDetail])): OrderDetailMatch = {
    if (sale._1.isDefined && sale._2.isDefined) {
      OrderDetailMatch.ORDER_DETAIL
    } else if (sale._1.isDefined) {
      OrderDetailMatch.ORDER_NULL
    } else {
      OrderDetailMatch.NULL_DETAIL
    }
  }

  private def ordersAndDetails2SaleDetails(client: Jedis,
                                           orderDetails: Iterable[(OrderDetailMatch, (Option[OrderInfo], Option[OrderDetail]))]): Iterable[SaleDetail] =
    orderDetails
      .map(_._2)
      .map(t => (t._1.get, t._2.get))
      .map {
        case (order, detail) =>
          client.setex(s"order:${order.id}", GmallConstants.REDIS_KEY_EXPIRE, JSON.toJSONString(order, SerializerFeature.IgnoreErrorGetter))
          new SaleDetail(order, detail)
      }

  private def orders2SaleDetails(client: Jedis,
                                 orders: Iterable[(OrderDetailMatch, (Option[OrderInfo], Option[OrderDetail]))]): Iterable[SaleDetail] =
    orders
      .map(_._2._1.get)
      .flatMap { order =>
        client.setex(s"order:${order.id}", GmallConstants.REDIS_KEY_EXPIRE, JSON.toJSONString(order, SerializerFeature.IgnoreErrorGetter))
        import scala.collection.JavaConversions._
        client
          .smembers(s"detail:${order.id}")
          .map(detail => new SaleDetail(order, JSON.parseObject(detail, classOf[OrderDetail])))
      }

  def details2SaleDetails(client: Jedis,
                          tuples: Iterable[(OrderDetailMatch, (Option[OrderInfo], Option[OrderDetail]))]): Iterable[SaleDetail] = {
    val details = tuples.map(_._2._2.get)
    details
      .filter(detail => !client.exists(s"order:${detail.order_id}"))
      .foreach(detail => {
        val key = s"detail:${detail.order_id}"
        client.sadd(key, JSON.toJSONString(detail, SerializerFeature.IgnoreErrorGetter))
        client.expire(key, GmallConstants.REDIS_KEY_EXPIRE)
      })
    details
      .filter(detail => client.exists(s"order:${detail.order_id}"))
      .map { detail => new SaleDetail(JSON.parseObject(client.get(s"order:${detail.order_id}"), classOf[OrderInfo]), detail) }
  }

}
