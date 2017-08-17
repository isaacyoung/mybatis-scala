package cn.isaac.utils

/**
  * Created by isaac on 17-8-17.
  */
object Bean {
  val jdbcFlag = "_@$# /&"

  def getClassName(name: String): String = {
    getJavaName(name, firstUpper = true)
  }

  def getFieldName(name: String): String = {
    getJavaName(name, firstUpper = false)
  }

  def getJavaName(name: String, firstUpper: Boolean): String = {
    var result = ""
    var upper = false

    name.foreach(x => {
      if (jdbcFlag.contains(x)) {
        upper = true
      } else if (upper) {
        result += x.toUpper
        upper = false
      } else {
        result += x
      }
    })

    if (firstUpper) {
      result = result.substring(0,1).toUpperCase + result.substring(1)
    }
    result
  }

  def getJdbcType(t: String): String = {
    val dataType = t.toUpperCase
    dataType match {
      case "INT" => "INTEGER"
      case "DATETIME" => "TIMESTAMP"
      case _ => dataType
    }
  }

  def getJavaType(t: String): String = {
    val dataType = t.toUpperCase
    dataType match {
      case "BIT" => "java.lang.Byte"
      case "TINYINT" => "java.lang.Byte"
      case "BOOL" => "java.lang.Boolean"
      case "BOOLEAN" => "java.lang.Boolean"
      case "SMALLINT" => "java.lang.Integer"
      case "MEDIUMINT" => "java.lang.Integer"
      case "INT" => "java.lang.Integer"
      case "INTEGER" => "java.lang.Integer"
      case "BIGINT" => "java.lang.Long"
      case "NUMERIC" => "java.math.BigDecimal"
      case "DECIMAL" => "java.math.BigDecimal"
      case "DEC" => "java.lang.Double"
      case "FIXED" => "java.lang.Double"
      case "FLOAT" => "java.lang.Double"
      case "DOUBLE" => "java.lang.Double"

      case "DATE" => "java.util.Date"
      case "DATETIME" => "java.util.Date"
      case "TIMESTAMP" => "java.util.Date"
      case "TIME" => "java.util.Date"
      case "YEAR" => "java.util.Date"

      case "CHAR" => "java.lang.String"
      case "VARCHAR" => "java.lang.String"
      case "BINARY" => "java.lang.String"
      case "VARBINARY" => "java.lang.String"
      case "TINYBLOB" => "java.lang.String"
      case "TINYTEXT" => "java.lang.String"
      case "BLOB" => "java.lang.String"
      case "TEXT" => "java.lang.String"
      case "MEDIUMBLOB" => "java.lang.String"
      case "MEDIUMTEXT" => "java.lang.String"
      case "LONGBLOB" => "java.lang.String"
      case "LONGTEXT" => "java.lang.String"
      case "ENUM" => "java.lang.String"
      case "SET" => "java.lang.String"
      case _ => "java.lang.Object"
    }
  }

  def getShortJavaType(t: String): String = {
    val fullType = getJavaType(t)
    fullType.substring(fullType.lastIndexOf(".") + 1)
  }

}
