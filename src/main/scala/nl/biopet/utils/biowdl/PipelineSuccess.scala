/*
 * Copyright (c) 2018 Biopet
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package nl.biopet.utils.biowdl

import org.testng.annotations.Test
import play.api.libs.json._

trait PipelineSuccess extends Pipeline {
  logMustHave("workflow finished with status 'Succeeded'".r)

  protected def parseFinalOutputs: JsObject = {
    val start = logLines.indexWhere(line =>
      line.matches(".*Workflow .* complete. Final Outputs:$")) + 1
    val end = logLines.indexWhere(_.startsWith("["), start)
    val jsonText = logLines.slice(start, end).mkString("\n")
    Json.parse(jsonText) match {
      case o: JsObject => o
      case _           => throw new IllegalArgumentException("Outputs should be a object")
    }
  }

  def expectedOutput: Map[String, JsValue]

  @Test(dependsOnGroups = Array("parseLog"))
  def testExpectedOutput(): Unit = {
    parseFinalOutputs.value shouldBe expectedOutput
  }
}
