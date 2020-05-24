package com.hiscat.gmall.realtime.util

import org.apache.spark.{SparkConf, SparkContext}

object Join {
  def main(args: Array[String]): Unit = {
    val sc = SparkContext.getOrCreate(new SparkConf().setAppName("join").setMaster("local[*]"))
    val a = sc.makeRDD(Seq(('a', 1), ('a', 2), ('b', 1), ('d', 2)))
    val b = sc.makeRDD(Seq(('b', 1), ('b', 2), ('a', 1), ('c', 2)))

    a.join(b).collect().foreach(println(_))
    println()
    a.rightOuterJoin(b).collect().foreach(println(_))
    println()
    a.fullOuterJoin(b).collect().foreach(println(_))

    sc.stop()

  }
}
