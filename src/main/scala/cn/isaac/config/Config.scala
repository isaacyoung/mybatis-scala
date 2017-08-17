package cn.isaac.config

import java.io.FileInputStream
import java.util.Properties

/**
  * Created by isaac on 17-8-17.
  */
class Config {

  private val p: Properties = {
    val properties = new Properties()
    val path = Thread.currentThread().getContextClassLoader.getResource("config.properties").getPath
    properties.load(new FileInputStream(path))
    properties
  }

}

object Config {
  var config: Config = new Config()

  def getDatabaseFromUrl: String = {
    val url = config.p.getProperty("jdbc.url")
    url.substring(url.lastIndexOf("/") + 1,url.indexOf("?"))
  }

  def getProperty(key: String): String = {
    config.p.getProperty(key)
  }
}
