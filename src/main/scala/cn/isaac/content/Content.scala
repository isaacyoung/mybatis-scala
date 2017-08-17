package cn.isaac.content

import cn.isaac.config.Config
import cn.isaac.jdbc.Connect

import scala.collection.mutable.ArrayBuffer

/**
  * Created by isaac on 17-8-17.
  */
class Content {
  var tables: ArrayBuffer[Table] = _

  def getProperty(key: String): String = {
    Config.getProperty(key)
  }

}

object Content {

  def build(): Content = {
    val content = new Content
    val connect = new Connect
    content.tables = connect.getTables()
    val columns = connect.getColumns()

    for (t <- content.tables) {
      t.columns = columns.filter(p => p.tableName == t.name)
      t.setClassName()
      t.setColumnValue()
    }

    content
  }
}