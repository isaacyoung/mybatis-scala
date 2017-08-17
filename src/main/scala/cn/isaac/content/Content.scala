package cn.isaac.content

import java.io.File

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

  def clearTarget(): Unit = {
    deleteDir(new File(getProperty("out.target")))
  }

  def deleteDir(dir: File): Unit = {
    val files = dir.listFiles()
    files.foreach(f => {
      if (f.isDirectory) {
        deleteDir(f)
      } else {
        f.delete()
      }
    })
    dir.delete()
  }

  def getModelPath(): String = {
    getProperty("out.target") + packageToPath(getProperty("pkg.model"))
  }

  def getDaoPath(): String = {
    getProperty("out.target") + packageToPath(getProperty("pkg.dao"))
  }

  def getServicePath(): String = {
    getProperty("out.target") + packageToPath(getProperty("pkg.serv"))
  }

  def getServiceImplPath(): String = {
    getProperty("out.target") + packageToPath(getProperty("pkg.serv"))  + "/impl"
  }

  def getXmlPath(): String = {
    getProperty("out.target") + packageToPath(getProperty("pkg.xml"))
  }

  def isOver(): Boolean = {
    getProperty("proj.over") == "true"
  }

  def packageToPath(pkg: String): String = {
    pkg.replace(".", "/")
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