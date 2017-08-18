package cn.isaac.content

import java.io.{BufferedWriter, File, FileWriter}

import cn.isaac.config.Config
import cn.isaac.jdbc.Connect

import scala.collection.mutable.ArrayBuffer
import scala.io.Source

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
    getProperty("out.target") + packageToPath(getProperty("pkg.serv")) + "/impl"
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

  def copyToProject(): Unit = {
    val fromPath = getProperty("out.target")
    val toPath = getProperty("proj.target")

    val toJavaPath = toPath + packageToPath("src.main.java.com")
    val toResourcePath = toPath + packageToPath("src.main.resources.mapper")

    new File(toJavaPath).mkdirs()
    copyDir(fromPath + "com", toJavaPath)

    new File(toResourcePath).mkdirs()
    copyDir(fromPath + "mapper", toResourcePath)
  }

  def copyDir(source: String, dest: String): Unit = {

    // get properties of source dir
    val sourceFile = new File(source)
    if (!sourceFile.exists()) {
      return
    }

    // create dest dir
    val destFile = new File(dest)
    if (!destFile.exists()) {
      destFile.mkdirs()
    }

    val directory = sourceFile.listFiles()

    for (obj <- directory) {

      val sourcefilepointer = source + "/" + obj.getName()
      val destinationfilepointer = dest + "/" + obj.getName()

      if (obj.isDirectory()) {
        // create sub-directories - recursively
        copyDir(sourcefilepointer, destinationfilepointer)
      } else {
        // perform copy
        copyFile(sourcefilepointer, destinationfilepointer)
      }

    }

  }

  def copyFile(source: String, dest: String): Unit = {
    val sourcefile = new File(source)

    val out = new BufferedWriter(new FileWriter(dest))
    Source.fromFile(sourcefile).getLines.foreach(s => {
      out.write(s, 0, s.length)
      out.newLine()
    })
    out.close()
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