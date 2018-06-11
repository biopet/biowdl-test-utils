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

package nl.biopet.utils.biowdl.multisample

import java.io.File

import nl.biopet.utils.conversions.{any2map, yamlFileToMap}
import nl.biopet.test.BiopetTest
import org.scalatest.exceptions.TestFailedException
import org.testng.annotations.Test

class MultisamplePipelineTest extends BiopetTest {
  @Test
  def testSampleConfig(): Unit = {
    val outDir = File.createTempFile("test.", ".yml")
    outDir.delete()
    outDir.mkdir()
    val pipeline = new MultisamplePipeline {

      override def samples: Map[String, Sample] =
        Map(
          "s1" -> Sample(
            "s1",
            Map("bla" -> "bla1"),
            libraries = Map(
              "l1" -> Library(
                "s1",
                "l1",
                Map("bla" -> "bla2"),
                readgroups = Map(
                  "r1" -> Readgroup("s1", "l1", "r1", Map("bla" -> "bla3")))))))

      /** File to start the pipeline from */
      def startFile: File = new File("test.wdl")
      override def outputDir: File = outDir
    }
    pipeline.inputs
    val config = yamlFileToMap(pipeline.sampleConfigFile)
    config shouldBe pipeline.sampleConfig
    config.keySet shouldBe Set("samples")
    val s1 = any2map(any2map(config("samples"))("s1"))
    s1("bla") shouldBe "bla1"
    val l1 = any2map(any2map(s1("libraries"))("l1"))
    l1("bla") shouldBe "bla2"
    val r1 = any2map(any2map(l1("readgroups"))("r1"))
    r1("bla") shouldBe "bla3"

    pipeline.sampleProvider.flatten.map(_.name) shouldBe Array("s1")
    pipeline.libraryProvider.flatten.map(_.sample) shouldBe Array("s1")
    pipeline.libraryProvider.flatten.map(_.library) shouldBe Array("l1")
    pipeline.readgroupProvider.flatten.map(_.sample) shouldBe Array("s1")
    pipeline.readgroupProvider.flatten.map(_.library) shouldBe Array("l1")
    pipeline.readgroupProvider.flatten.map(_.readgroup) shouldBe Array("r1")

    intercept[TestFailedException] {
      pipeline.testSampleDir(pipeline.samples("s1"))
    }
    pipeline.sampleDir(pipeline.samples("s1")).mkdirs()
    pipeline.testSampleDir(pipeline.samples("s1"))

    intercept[TestFailedException] {
      pipeline.testLibraryDir(pipeline.samples("s1").libraries("l1"))
    }
    pipeline.libraryDir(pipeline.samples("s1").libraries("l1")).mkdirs()
    pipeline.testLibraryDir(pipeline.samples("s1").libraries("l1"))

    intercept[TestFailedException] {
      pipeline.testReadgroupDir(
        pipeline.samples("s1").libraries("l1").readgroups("r1"))
    }
    pipeline
      .readgroupDir(pipeline.samples("s1").libraries("l1").readgroups("r1"))
      .mkdirs()
    pipeline.testReadgroupDir(
      pipeline.samples("s1").libraries("l1").readgroups("r1"))
  }
}
