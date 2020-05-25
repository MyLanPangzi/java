package com.hiscat.gmall.realtime.bean

import scala.beans.BeanProperty

case class OrderInfo(
                      @BeanProperty    id: String,
                      @BeanProperty    province_id: String,
                      @BeanProperty    consignee: String,
                      @BeanProperty    order_comment: String,
                      @BeanProperty    var consignee_tel: String,
                      @BeanProperty    order_status: String,
                      @BeanProperty    payment_way: String,
                      @BeanProperty    user_id: String,
                      @BeanProperty    img_url: String,
                      @BeanProperty    total_amount: Double,
                      @BeanProperty    expire_time: String,
                      @BeanProperty    delivery_address: String,
                      @BeanProperty    create_time: String,
                      @BeanProperty    operate_time: String,
                      @BeanProperty    tracking_no: String,
                      @BeanProperty    parent_order_id: String,
                      @BeanProperty    out_trade_no: String,
                      @BeanProperty    trade_body: String,
                      @BeanProperty    var create_date: String,
                      @BeanProperty    var create_hour: String)