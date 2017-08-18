package cn.isaac.code

import java.io.{File, PrintWriter}

import cn.isaac.content.Content

import scala.collection.mutable.ArrayBuffer

/**
  * Created by isaac on 17-8-17.
  */
object Impl {
  def code(content: Content): Unit = {
    val target = new File(content.getServiceImplPath())
    if (!target.exists()) {
      target.mkdirs()
    }

    for (t <- content.tables) {
      val writer = new PrintWriter(new File(content.getServiceImplPath() + "/" + t.getServiceImplName() + ".java"))
      writer.write("package " + content.getProperty("pkg.serv") + ".impl;\n\n")

      var imports = new ArrayBuffer[String]
      imports += "com.cdsq.manage.base.BaseServiceImp"
      imports += "org.springframework.stereotype.Service"
      imports += content.getProperty("pkg.model") + "." + t.className
      imports += content.getProperty("pkg.dao") + "." + t.getDaoName()
      imports += content.getProperty("pkg.serv") + "." + t.getServiceName()
      imports = imports.distinct.sorted
      for (v <- imports) {
        writer.write("import " + v + ";\n")
      }
      writer.write("\n")

      writer.write("/**\n")
      writer.write(" * " + t.comment + "\n")
      writer.write(" */\n")
      writer.write("@Service\n")
      writer.write("public class " + t.getServiceImplName() + " extends BaseServiceImp<" + t.className + ", " + t.getDaoName() + "> implements " + t.getServiceName() + " {\n")

      writer.write("}")

      writer.close()
    }
  }
}
