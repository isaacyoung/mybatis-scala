package cn.isaac.code

import java.io.{File, PrintWriter}

import cn.isaac.content.Content

import scala.collection.mutable.ArrayBuffer

/**
  * Created by isaac on 17-8-17.
  */
object Serv {
  def code(content: Content): Unit = {
    val target = new File(content.getServicePath())
    if (!target.exists()) {
      target.mkdirs()
    }

    for (t <- content.tables) {
      val writer = new PrintWriter(new File(content.getServicePath() + "/" + t.getServiceName() + ".java"))
      writer.write("package " + content.getProperty("pkg.serv") + ";\n\n")

      var imports = new ArrayBuffer[String]
      imports += "com.cdsq.manage.base.BaseService"
      imports += content.getProperty("pkg.model") + "." + t.className
      imports = imports.distinct.sorted
      for (v <- imports) {
        writer.write("import " + v + ";\n")
      }
      writer.write("\n")

      writer.write("/**\n")
      writer.write(" * " + t.comment + "\n")
      writer.write(" */\n")
      writer.write("public interface " + t.getServiceName() + " extends BaseService<" + t.className + "> {\n")

      writer.write("}")

      writer.close()
    }
  }
}
