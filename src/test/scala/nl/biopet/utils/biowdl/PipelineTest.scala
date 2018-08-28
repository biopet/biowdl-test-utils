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
import org.testng.annotations.Test

import scala.util.matching.Regex

class PipelineTest extends BiopetTest {

  @Test
  def testLogMustHave(): Unit = {
    val outDir = File.createTempFile("test.", ".yml")
    outDir.delete()
    outDir.mkdir()

    val r: Regex = ".*".r

    val pipeline = new Pipeline {
      override def startFile: File = new File(".")
      logMustHave(r)
      override def outputDir: File = outDir
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
      override def startFile: File = new File(".")
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
      override def startFile: File = new File(".")
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
      override def startFile: File = new File(".")
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
    ???
  }

}
