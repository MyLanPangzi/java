package com.hiscat.gmall.realtime.bean

import scala.beans.BeanProperty

case class UserInfo(
                     @BeanProperty id: String,
                     @BeanProperty login_name: String,
                     @BeanProperty user_level: String,
                     @BeanProperty birthday: String,
                     @BeanProperty gender: String)