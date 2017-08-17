package cn.isaac.content

import cn.isaac.utils.Bean

import scala.collection.mutable.ArrayBuffer

/**
  * Created by isaac on 17-8-17.
  */
class Table {
  var name = ""
  var comment = ""
  var className = ""
  var columns: ArrayBuffer[Column] = _

  def setClassName(): Unit = {
    if (name == "") {
      return
    }
    className = Bean.getClassName(name)
  }

  def setColumnValue(): Unit = {
    if (columns == null || columns.size == 0) {
      return
    }

    for (c <- columns) {
      c.fieldName = Bean.getFieldName(c.name)
      c.jdbcType = Bean.getJdbcType(c.dataType)
      c.javaType = Bean.getJavaType(c.dataType)
      c.shortJavaType = Bean.getShortJavaType(c.dataType)
    }
  }

  def getImportType(): ArrayBuffer[String] = {
    columns.filter(p => !p.javaType.startsWith("java.lang")).map(f => f.javaType).distinct
  }

  def getDaoName(): String = {
    return className + "Mapper"
  }

  def getServiceName(): String = {
    return className + "Service"
  }

  def getServiceImplName(): String = {
    return className + "ServiceImp"
  }

  def getKeys(): ArrayBuffer[Column] = {
    columns.filter(p => p.key)
  }

}
