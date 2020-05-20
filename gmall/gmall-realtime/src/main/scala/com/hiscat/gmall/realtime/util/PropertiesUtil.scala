package com.hiscat.gmall.realtime.util

import java.io.InputStreamReader
import java.util.Properties

object PropertiesUtil {

  def load(fileName: String): Properties = {
    val prop = new Properties()
    prop.load(new InputStreamReader(Thread.currentThread().getContextClassLoader.getResourceAsStream(fileName), "UTF-8"))
    prop
  }
}