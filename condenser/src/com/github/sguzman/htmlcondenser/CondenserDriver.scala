package com.github.sguzman.htmlcondenser

import scala.io.Source

/**
  * Simple driver for testing purposes
  */
object CondenserDriver {
  def main(args: Array[String]): Unit = {
    val path = "/Users/salvadorguzman/Software/scala/HtmlCondenser/condenser/test/resources/comments/comments-input.html"
    val html = Source.fromFile(path).getLines.mkString("\n")
    val condensed = Condenser.condenseString(html)

    println("Input:")
    println(html)

    println("Output:")
    println(s"length = ${condensed.length}")
    println(condensed)
  }
}
