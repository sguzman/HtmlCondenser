package com.github.sguzman.htmlcondenser

import org.scalatest.FlatSpec

import scala.io.Source

class CondenserTest extends FlatSpec {
  def testExample(name: String): Unit = {
    val inputPath = s"./condenser/test/resources/$name/$name-input.html"
    val outputPath = s"./condenser/test/resources/$name/$name-output.html"

    val input = Source.fromFile(inputPath).getLines.mkString("\n")
    val actual = Condenser.condenseString(input)

    val output = Source.fromFile(outputPath).getLines.mkString("\n")

    assert(actual == output)
  }

  "An HTML condenser" should "remove comments" in {
    testExample("comments")
  }

  "An HTML condenser" should "handle simple input" in {
    testExample("simple")
  }
}
