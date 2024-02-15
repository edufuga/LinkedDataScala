package com.edufuga.scala.data.access.ops

import fs2.io.file.Path

object FileOps {
  def pathOf(file: String): Path = {
    val readFromJavaPath: java.nio.file.Path = java.nio.file.Paths.get(file).toAbsolutePath
    Path.fromNioPath(readFromJavaPath)
  }
}
