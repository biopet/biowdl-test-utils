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

import java.io.File

import nl.biopet.test.BiopetTest
import org.scalatest.exceptions.TestFailedException
import org.testng.annotations.Test
import play.api.libs.json._

import scala.util.matching.Regex

class PipelineTest extends BiopetTest {

  @Test
  def testLogMustHave(): Unit = {
    val outDir = File.createTempFile("test.", ".yml")
    outDir.delete()
    outDir.mkdir()

    val r: Regex = ".*".r

    val pipeline = new Pipeline {
      def startFile: File = new File(".")
      logMustHave(r)
      override def outputDir: File = outDir

      def expectedOutput: Map[String, JsValue] = Map()
    }

    pipeline.logMustHaveProvider shouldBe Array(Array(r))
  }

  @Test
  def testLogMustNotHave(): Unit = {
    val outDir = File.createTempFile("test.", ".yml")
    outDir.delete()
    outDir.mkdir()

    val r: Regex = ".*".r

    val pipeline = new Pipeline {
      def startFile: File = new File(".")
      def expectedOutput: Map[String, JsValue] = Map()
      logMustNotHave(r)
      override def outputDir: File = outDir
    }

    pipeline.logMustNotHaveProvider shouldBe Array(Array(r))
  }

  @Test
  def testCreateFile(): Unit = {
    val outDir = File.createTempFile("test.", ".yml")
    outDir.delete()
    outDir.mkdir()

    val pipeline = new Pipeline {
      def startFile: File = new File(".")
      def expectedOutput: Map[String, JsValue] = Map()
      val file = createFile("bla")
      override def outputDir: File = outDir
    }

    pipeline.file shouldBe new File(pipeline.outputDir, "bla")
    pipeline.mustHaveFilesProvider shouldBe Array(
      Array(new File(pipeline.outputDir, "bla")))
  }

  @Test
  def testCreateOptionalFile(): Unit = {
    val outDir = File.createTempFile("test.", ".yml")
    outDir.delete()
    outDir.mkdir()

    val pipeline = new Pipeline {
      def startFile: File = new File(".")
      def expectedOutput: Map[String, JsValue] = Map()
      val file = createOptionalFile(true, "true")
      val fileNot = createOptionalFile(false, "false")
      override def outputDir: File = outDir
    }

    pipeline.file shouldBe Some(new File(pipeline.outputDir, "true"))
    pipeline.mustHaveFilesProvider shouldBe Array(
      Array(new File(pipeline.outputDir, "true")))
    pipeline.fileNot shouldBe None
    pipeline.mustNotHaveFilesProvider shouldBe Array(
      Array(new File(pipeline.outputDir, "false")))
  }

  @Test
  def testParseOutputs(): Unit = {
    val pipeline = new Pipeline {
      def startFile: File = new File(".")
      def expectedOutput: Map[String, JsValue] = Map()

      override def logLines: List[String] =
        List(
          "[2018-08-28 15:09:39,92] [info] WorkflowExecutionActor-9fdca1ae-805e-4c4d-8421-09c7023bff3e [9fdca1ae]: Workflow AlignStar complete. Final Outputs:",
          "{",
          "  \"k1\": \"v1\",",
          "  \"k2\": \"v2\",",
          "  \"k3\": 3,",
          "  \"k4\": true,",
          "  \"k5\": null",
          "}",
          "[2018-08-28 15:09:39,96] [info] WorkflowManagerActor WorkflowActor-9fdca1ae-805e-4c4d-8421-09c7023bff3e is in a terminal state: WorkflowSucceededState"
        )

      def test = parseFinalOutputs
    }

    val outputs = pipeline.test
    outputs.value.keySet shouldBe Set("k1", "k2", "k3", "k4", "k5")
    outputs.value("k1") shouldBe JsString("v1")
    outputs.value("k2") shouldBe JsString("v2")
    outputs.value("k3") shouldBe JsNumber(3)
    outputs.value("k4") shouldBe JsBoolean(true)
    outputs.value("k5") shouldBe JsNull
  }

  @Test
  def testExpectedOutputCorrect(): Unit = {
    val pipeline = new Pipeline {
      def startFile: File = new File(".")
      def expectedOutput: Map[String, JsValue] = Map(
        "k1" -> JsString("v1"),
        "k2" -> JsString("v2"),
        "k3" -> JsNumber(3),
        "k4" -> JsBoolean(true),
        "k5" -> JsNull
      )

      override def logLines: List[String] =
        List(
          "[2018-08-28 15:09:39,92] [info] WorkflowExecutionActor-9fdca1ae-805e-4c4d-8421-09c7023bff3e [9fdca1ae]: Workflow AlignStar complete. Final Outputs:",
          "{",
          "  \"k1\": \"v1\",",
          "  \"k2\": \"v2\",",
          "  \"k3\": 3,",
          "  \"k4\": true,",
          "  \"k5\": null",
          "}",
          "[2018-08-28 15:09:39,96] [info] WorkflowManagerActor WorkflowActor-9fdca1ae-805e-4c4d-8421-09c7023bff3e is in a terminal state: WorkflowSucceededState"
        )

      def test = parseFinalOutputs
    }

    pipeline.testExpectedOutput()
  }

  @Test
  def testExpectedOutputIncorrect(): Unit = {
    val pipeline = new Pipeline {
      def startFile: File = new File(".")

      def expectedOutput: Map[String, JsValue] = Map(
        "k1" -> JsString("v1"),
        "k2" -> JsString("v2"),
        "k3" -> JsNumber(3),
        "k4" -> JsBoolean(true),
        "k5" -> JsNull
      )

      override def logLines: List[String] =
        List(
          "[2018-08-28 15:09:39,92] [info] WorkflowExecutionActor-9fdca1ae-805e-4c4d-8421-09c7023bff3e [9fdca1ae]: Workflow AlignStar complete. Final Outputs:",
          "{",
          "  \"k5\": null",
          "}",
          "[2018-08-28 15:09:39,96] [info] WorkflowManagerActor WorkflowActor-9fdca1ae-805e-4c4d-8421-09c7023bff3e is in a terminal state: WorkflowSucceededState"
        )

      def test = parseFinalOutputs
    }

    intercept[TestFailedException] {
      pipeline.testExpectedOutput()
    }
  }

}
