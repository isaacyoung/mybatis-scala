package cn.isaac.jdbc

import java.sql.{Connection, DriverManager}

import cn.isaac.config.Config
import cn.isaac.content.{Column, Table}

import scala.collection.mutable.ArrayBuffer

/**
  * Created by isaac on 17-8-17.
  */
class Connect {
  val driver = Config.getProperty("jdbc.driver")
  val url = Config.getProperty("jdbc.url")
  val username = Config.getProperty("jdbc.username")
  val password = Config.getProperty("jdbc.password")
  var tableName = Config.getProperty("jdbc.table")

  def getTables(): ArrayBuffer[Table] = {
    var connection: Connection = null

    try {
      Class.forName(driver)
      connection = DriverManager.getConnection(url, username, password)
      val statement = connection.createStatement()
      var sql = "SELECT TABLE_NAME,TABLE_COMMENT FROM information_schema.`TABLES` "
      sql += "WHERE TABLE_SCHEMA='" + Config.getDatabaseFromUrl + "' AND TABLE_TYPE='BASE TABLE'"
      if (tableName != null && tableName != "" && tableName != "%") {
        sql += "AND TABLE_NAME='" + tableName + "'"
      }

      val resultSet = statement.executeQuery(sql)
      val result = new ArrayBuffer[Table]()
      while (resultSet.next()) {
        val table = new Table {}
        table.name = resultSet.getString("TABLE_NAME")
        table.comment = resultSet.getString("TABLE_COMMENT")
        result.append(table)
      }
      return result
    }
    catch {
      case e: Exception => e.printStackTrace()
    } finally {
      if (connection != null) {
        connection.close()
      }
    }
    null
  }

  def getColumns(): ArrayBuffer[Column] = {
    var connection: Connection = null

    try {
      Class.forName(driver)
      connection = DriverManager.getConnection(url, username, password)
      val statement = connection.createStatement()
      var sql = "SELECT TABLE_NAME,COLUMN_NAME,COLUMN_KEY,DATA_TYPE,IFNULL(NUMERIC_SCALE,0) AS NUMERIC_SCALE,IFNULL(CHARACTER_MAXIMUM_LENGTH,0) AS CHARACTER_MAXIMUM_LENGTH,COLUMN_COMMENT FROM information_schema.`COLUMNS` "
      sql += "WHERE TABLE_SCHEMA='" + Config.getDatabaseFromUrl + "' "
      if (tableName != null && tableName != "" && tableName != "%") {
        sql += "AND TABLE_NAME='" + tableName + "'"
      }

      val resultSet = statement.executeQuery(sql)
      val result = new ArrayBuffer[Column]()
      while (resultSet.next()) {
        val column = new Column {}
        column.tableName = resultSet.getString("TABLE_NAME")
        column.name = resultSet.getString("COLUMN_NAME")
        column.key = if (resultSet.getString("COLUMN_KEY") != null && resultSet.getString("COLUMN_KEY") == "PRI") true else false
        column.dataType = resultSet.getString("DATA_TYPE")
        column.scale = resultSet.getInt("NUMERIC_SCALE")
        column.length = resultSet.getLong("CHARACTER_MAXIMUM_LENGTH")
        column.comment = resultSet.getString("COLUMN_COMMENT")
        result.append(column)
      }
      return result
    }
    catch {
      case e: Exception => e.printStackTrace()
    } finally {
      if (connection != null) {
        connection.close()
      }
    }
    null
  }
}
