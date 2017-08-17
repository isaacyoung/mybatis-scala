package cn.isaac.code

import java.io.{File, PrintWriter}

import cn.isaac.content.Content

import scala.collection.mutable.ArrayBuffer

/**
  * Created by isaac on 17-8-17.
  */
object Dao {
  def code(content: Content): Unit = {
    val target = new File(content.getDaoPath())
    if (!target.exists()) {
      target.mkdirs()
    }

    for (t <- content.tables) {
      val writer = new PrintWriter(new File(content.getDaoPath() + "/" + t.getDaoName() + ".java"))
      writer.write("package " + content.getProperty("pkg.dao") + ";\n\n")

      var imports = new ArrayBuffer[String]
      imports += "com.cdsq.manage.base.BaseMapper"
      imports += content.getProperty("pkg.model") + "." + t.className
      imports = imports.distinct.sorted
      for (v <- imports) {
        writer.write("import " + v + ";\n")
      }
      writer.write("\n")

      writer.write("/**\n")
      writer.write(" * " + t.comment + "\n")
      writer.write(" */\n")
      writer.write("public interface " + t.getDaoName() + " extends BaseMapper<" + t.className + "> {\n")

      writer.write("}")

      writer.close()
    }
  }
}
