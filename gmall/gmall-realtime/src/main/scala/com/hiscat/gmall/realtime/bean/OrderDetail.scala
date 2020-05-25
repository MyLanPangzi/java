package com.hiscat.gmall.realtime.bean

import scala.beans.BeanProperty

case class OrderDetail(
                        @BeanProperty             id:String,
                        @BeanProperty        order_id: String,
                        @BeanProperty        sku_name: String,
                        @BeanProperty        sku_id: String,
                        @BeanProperty        order_price: String,
                        @BeanProperty        img_url: String,
                        @BeanProperty        sku_num: String)