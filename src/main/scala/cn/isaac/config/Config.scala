package cn.isaac.config

import java.io.FileInputStream
import java.util.Properties

/**
  * Created by isaac on 17-8-17.
  */
object Config {

  val config: Properties = init

  def init: Properties = {
    val properties = new Properties()
    val path = Thread.currentThread().getContextClassLoader.getResource("config.properties").getPath
    properties.load(new FileInputStream(path))
    properties
  }

  def getConfig: Properties = {
    config
  }

  def getDatabaseFromUrl: String = {
    val url = config.getProperty("jdbc.url")
    url.substring(url.lastIndexOf("/") + 1,url.indexOf("?"))
  }
}
