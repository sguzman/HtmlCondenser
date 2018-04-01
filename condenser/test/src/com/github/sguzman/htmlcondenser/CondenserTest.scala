package com.github.sguzman.htmlcondenser

import org.scalatest.FlatSpec

import scala.io.Source

class CondenserTest extends FlatSpec {
  "An HTML condenser" should "remove comments" in {
    val inputPath = "./condenser/test/resources/comments/comments-input.html"
    val outputPath = "./condenser/test/resources/comments/comments-output.html"

    val input = Source.fromFile(inputPath).getLines.mkString("\n")
    val actual = Condenser.condenseString(input)

    val output = Source.fromFile(outputPath).getLines.mkString("\n")

    assert(actual == output)
  }
}
