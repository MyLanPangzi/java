package com.hiscat.gmall.realtime.bean

case class CouponAlertInfo(mid: String,
                           uids: java.util.Set[String],
                           itemIds: java.util.Set[String],
                           events: java.util.List[String],
                           ts: Long)