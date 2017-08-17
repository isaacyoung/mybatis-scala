package cn.isaac

import cn.isaac.config.Config


/**
  * Created by isaac on 17-8-17.
  */
object main {
  def main(args: Array[String]): Unit = {
    val config = Config.getConfig
    print(config.getProperty("jdbc.driver"))
  }
}
