package cn.isaac

import cn.isaac.code.Model
import cn.isaac.content.Content


/**
  * Created by isaac on 17-8-17.
  */
object main {
  def main(args: Array[String]): Unit = {

    // config
    val content = Content.build()

    content.clearTarget()

    // code
    Model.code(content)

    // copy
    if (content.isOver()) {

    }

  }
}
