package cn.isaac.code

import java.io.{File, PrintWriter}

import cn.isaac.content.{Column, Content}

import scala.collection.mutable.ArrayBuffer

/**
  * Created by isaac on 17-8-17.
  */
object Xml {
  def code(content: Content): Unit = {
    val target = new File(content.getXmlPath())
    if (!target.exists()) {
      target.mkdirs()
    }

    for (t <- content.tables) {
      val writer = new PrintWriter(new File(content.getXmlPath() + "/" + t.getDaoName() + ".xml"))
      writer.write("""<?xml version="1.0" encoding="UTF-8"?>""" + "\n")
      writer.write("""<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">""" + "\n")
      writer.write("""<mapper namespace="""" + content.getProperty("pkg.dao") + """.""" + t.getDaoName() + """">""" + "\n")

      // BaseResultMap
      writer.write("""  <resultMap id="BaseResultMap" type="""" + content.getProperty("pkg.model") + """.""" + t.className + """">""" + "\n")
      for (c <- t.columns) {
        if (c.isKey()) {
          writer.write("""    <id column="""" + c.name + """" jdbcType="""" + c.jdbcType + """" property="""" + c.fieldName + """" />""" + "\n")
        } else {
          writer.write("""    <result column="""" + c.name + """" jdbcType="""" + c.jdbcType + """" property="""" + c.fieldName + """" />""" + "\n")
        }
      }
      writer.write("""  </resultMap>""" + "\n\n")

      // selectById
      writer.write("""  <select id="selectById" parameterType="java.lang.Integer" resultMap="BaseResultMap">""" + "\n")
      writer.write("""    select * from """ + t.name + "\n")
      val keys = t.getKeys()
      for ((v, i) <- keys.zipWithIndex) {
        if (i == 0) {
          writer.write("    where ")
        } else {
          writer.write(" and ")
        }
        writer.write(v.name + " = #{" + v.fieldName + ",jdbcType=" + v.jdbcType + "}")
      }
      writer.write("\n")
      writer.write("""  </select>""" + "\n\n")

      // deleteById
      writer.write("""  <delete id="deleteById" parameterType="java.lang.Integer">""" + "\n")
      writer.write("""    delete from """ + t.name + "\n")
      for ((v, i) <- keys.zipWithIndex) {
        if (i == 0) {
          writer.write("    where ")
        } else {
          writer.write(" and ")
        }
        writer.write(v.name + " = #{" + v.fieldName + ",jdbcType=" + v.jdbcType + "}")
      }
      writer.write("\n")
      writer.write("""  </delete>""" + "\n\n")

      // select
      writer.write("""  <select id="select" parameterType="""" + content.getProperty("pkg.model") + """.""" + t.className + """" resultMap="BaseResultMap">""" + "\n")
      writer.write("""    select """ + "\n")
      writer.write("""    a.*""" + "\n")
      writer.write("""    from """ + t.name + " a\n")
      writer.write("""    <trim prefix="where" prefixOverrides="and|or">""" + "\n")
      for ((c, i) <- t.columns.zipWithIndex) {
        if (c.isString()) {
          writer.write("""      <if test="""" + c.fieldName + """ != null and """ + c.fieldName + """ !=  ''">""" + "\n")
        } else if (c.isDate()) {
          writer.write("""      <if test="""" + c.fieldName + """ != null ">""" + "\n")
        } else {
          writer.write("""      <if test="""" + c.fieldName + """ != null and """ + c.fieldName + """ !=  -1 ">""" + "\n")
        }
        if (i == 0) {
          writer.write("""        """)
        } else {
          writer.write("""        and """)
        }
        writer.write("""a.""" + c.name + " = #{" + c.fieldName + ",jdbcType=" + c.jdbcType + "}" + "\n")
        writer.write("""      </if>""" + "\n")
      }

      val str =
        """      <if test="sqlCondition != null and sqlCondition.size > 0">
        and
        <foreach collection="sqlCondition" index="index" item="condition" separator=" and ">
          ${condition}
        </foreach>
      </if>"""
      writer.write(str + "\n")
      writer.write("""    </trim>""" + "\n")
      writer.write("""  </select>""" + "\n\n")

      // insert
      writer.write("""  <insert id="insert" keyProperty="id" parameterType="""" + content.getProperty("pkg.model") + """.""" + t.className + """" useGeneratedKeys="true">""" + "\n")
      val sql = getInsertSql(t.columns)
      writer.write("""    insert into """ + t.name + " (" + sql(0) + ")\n")
      writer.write("""    values""" + "\n")
      writer.write("""    <foreach collection="list" index="index" item="item" separator=",">""" + "\n")
      writer.write("       (" + sql(1) + ")\n")
      writer.write("""    </foreach>""" + "\n")
      writer.write("""  </insert>""" + "\n\n")

      // update
      writer.write("""  <update id="update" parameterType="""" + content.getProperty("pkg.model") + """.""" + t.className + """">""" + "\n")
      writer.write("""    <foreach close="" collection="list" index="index" item="item" open="" separator=";">""" + "\n")
      writer.write("""      update """ + t.name + "\n")
      writer.write("""      <set>""" + "\n")
      for ((c, i) <- t.columns.zipWithIndex) {
        if (!c.isKey()) {
          if (c.isString()) {
            writer.write("""        <if test="item.""" + c.fieldName + """ != null and item.""" + c.fieldName + """ !=  ''">""" + "\n")
          } else if (c.isDate()) {
            writer.write("""        <if test="item.""" + c.fieldName + """ != null ">""" + "\n")
          } else {
            writer.write("""        <if test="item.""" + c.fieldName + """ != null and item.""" + c.fieldName + """ !=  -1 ">""" + "\n")
          }

          writer.write("""          """ + c.name + " = #{item." + c.fieldName + ",jdbcType=" + c.jdbcType + "}")
          if (i != t.columns.length - 1) {
            writer.write(",")
          }
          writer.write("\n")
          writer.write("""        </if>""" + "\n")
        }
      }
      writer.write("""      </set>""" + "\n")
      for ((v, i) <- keys.zipWithIndex) {
        if (i == 0) {
          writer.write("      where ")
        } else {
          writer.write(" and ")
        }
        writer.write(v.name + " = #{item." + v.fieldName + ",jdbcType=" + v.jdbcType + "}")
      }
      writer.write("\n")

      writer.write("""    </foreach>""" + "\n")
      writer.write("""  </update>""" + "\n\n")

      // delete
      writer.write("""  <delete id="delete" parameterType="""" + content.getProperty("pkg.model") + """.""" + t.className + """">""" + "\n")
      writer.write("""    delete from """ + t.name + "\n")
      writer.write("""    <trim prefix="where" prefixOverrides="and|or">""" + "\n")
      for ((c, i) <- t.columns.zipWithIndex) {
        if (c.isString()) {
          writer.write("""      <if test="""" + c.fieldName + """ != null and """ + c.fieldName + """ !=  ''">""" + "\n")
        } else if (c.isDate()) {
          writer.write("""      <if test="""" + c.fieldName + """ != null ">""" + "\n")
        } else {
          writer.write("""      <if test="""" + c.fieldName + """ != null and """ + c.fieldName + """ !=  -1 ">""" + "\n")
        }

        if (i == 0) {
          writer.write("""        """)
        } else {
          writer.write("""        and """)
        }
        writer.write(c.name + " = #{" + c.fieldName + ",jdbcType=" + c.jdbcType + "}\n")

        writer.write("""      </if>""" + "\n")
      }

      writer.write("""    </trim>""" + "\n")
      writer.write("""  </delete>""" + "\n\n")

      writer.write("""</mapper>""")
      writer.close()
    }
  }

  def getInsertSql(columns: ArrayBuffer[Column]): Array[String] = {
    val result = new Array[String](2)
    val list = new StringBuilder
    val values = new StringBuilder
    val temp = new StringBuilder
    for ((v, i) <- columns.zipWithIndex) {
      if (temp.length > 80) {
        list.append("\n      ")
        values.append("\n        ")
        temp.clear()
      }
      list.append(v.name)
      values.append("#{item." + v.fieldName + ",jdbcType=" + v.jdbcType + "}")
      temp.append("#{item." + v.fieldName + ",jdbcType=" + v.jdbcType + "}")

      if (i != columns.length - 1) {
        list.append(", ")
        values.append(", ")
        temp.append(", ")
      }
    }

    result(0) = list.toString()
    result(1) = values.toString()
    return result
  }

}
