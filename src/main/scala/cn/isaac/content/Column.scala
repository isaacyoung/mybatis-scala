package cn.isaac.content

/**
  * Created by isaac on 17-8-17.
  */
class Column {
  var tableName = ""
  var name = ""
  var key = false
  var dataType = ""
  var scale = 0
  var length = 0l
  var comment = ""

  var fieldName = ""
  var jdbcType = ""
  var javaType = ""
  var shortJavaType = ""

  def getFieldGetter(): String = {
    "get" + fieldName.substring(0,1).toUpperCase + fieldName.substring(1)
  }

  def getFieldSetter(): String = {
    "set" + fieldName.substring(0,1).toUpperCase + fieldName.substring(1)
  }

}
