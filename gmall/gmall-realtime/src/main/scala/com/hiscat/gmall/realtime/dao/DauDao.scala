package com.hiscat.gmall.realtime.dao

import com.hiscat.gmall.realtime.bean.StartUpLog
import com.hiscat.gmall.realtime.util.RedisUtil
import org.apache.spark.streaming.dstream.DStream

object DauDao {
  def writeRedis(events: DStream[StartUpLog]): Unit = {
    events.foreachRDD(rdd => {
      rdd.foreachPartition(it => {
        val client = RedisUtil.getJedisClient

        it.foreach(e => {
          client.sadd(s"Dau:${e.logDate}", e.mid)
        })

        client.close()
      })
    })
  }

}
