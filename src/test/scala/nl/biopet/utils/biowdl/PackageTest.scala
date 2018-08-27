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

class PackageTest extends BiopetTest {
  @Test
  def testGlobalOutputDir(): Unit = {
    System.clearProperty("biowdl.outputDir")
    intercept[IllegalArgumentException] {
      globalOutputDir
    }.getMessage shouldBe "No output_dir found, please set the 'biowdl.outputDir' property"
    System.setProperty("biowdl.outputDir", "test")
    globalOutputDir shouldBe new File("test")
  }

  @Test
  def testFunctionalTests(): Unit = {
    System.clearProperty("biowdl.functionalTests")
    functionalTests shouldBe false
    System.setProperty("biowdl.functionalTests", "false")
    functionalTests shouldBe false
    System.setProperty("biowdl.functionalTests", "true")
    functionalTests shouldBe true

    System.setProperty("biowdl.functionalTests", "random")
    intercept[IllegalArgumentException] {
      functionalTests
    }.getMessage shouldBe "for 'biowdl.functionalTests' only true or false is allowed, found: random"
  }

  @Test
  def testIntegrationTests(): Unit = {
    System.clearProperty("biowdl.integrationTests")
    integrationTests shouldBe true
    System.setProperty("biowdl.integrationTests", "false")
    integrationTests shouldBe false
    System.setProperty("biowdl.integrationTests", "true")
    integrationTests shouldBe true

    System.setProperty("biowdl.integrationTests", "random")
    intercept[IllegalArgumentException] {
      integrationTests
    }.getMessage shouldBe "for 'biowdl.integrationTests' only true or false is allowed, found: random"
  }

  @Test
  def testZipped(): Unit = {
    System.clearProperty("biowdl.zipped")
    zipped shouldBe false
    System.setProperty("biowdl.zipped", "false")
    zipped shouldBe false
    System.setProperty("biowdl.zipped", "true")
    zipped shouldBe true

    System.setProperty("biowdl.zipped", "random")
    intercept[IllegalArgumentException] {
      zipped
    }.getMessage shouldBe "for 'biowdl.zipped' only true or false is allowed, found: random"
  }

  @Test
  def testFixtureDir(): Unit = {
    System.clearProperty("biowdl.fixtureDir")
    intercept[IllegalArgumentException] {
      fixtureDir
    }.getMessage shouldBe "No output_dir found, please set the 'biowdl.fixtureDir' property"

    val dir = File.createTempFile("test.", ".test")
    System.setProperty("biowdl.fixtureDir", dir.getAbsolutePath)
    intercept[IllegalArgumentException] {
      fixtureDir
    }.getMessage shouldBe s"requirement failed: Fixture directory is not a directory: ${dir.getAbsolutePath}"

    dir.delete()
    intercept[IllegalArgumentException] {
      fixtureDir
    }.getMessage shouldBe s"requirement failed: Fixture directory does not exist: ${dir.getAbsolutePath}"

    dir.mkdir()
    fixtureDir shouldBe dir

  }

  @Test
  def testFixtureFile(): Unit = {
    System.clearProperty("biowdl.fixtureDir")
    intercept[IllegalArgumentException] {
      fixtureFile("bla.txt")
    }.getMessage shouldBe "No output_dir found, please set the 'biowdl.fixtureDir' property"

    val dir = File.createTempFile("test.", ".test")
    dir.delete()
    dir.mkdir()
    System.setProperty("biowdl.fixtureDir", dir.getAbsolutePath)

    intercept[IllegalArgumentException] {
      fixtureFile("bla.txt")
    }.getMessage shouldBe s"requirement failed: Fixture file does not exist: ${dir.getAbsolutePath}/bla.txt"

    val file = new File(dir, "bla.txt")
    file.createNewFile()
    fixtureFile("bla.txt") shouldBe file

    file.setReadable(false, false)

    intercept[IllegalArgumentException] {
      fixtureFile("bla.txt")
    }.getMessage shouldBe s"requirement failed: Fixture file is not readable: ${dir.getAbsolutePath}/bla.txt"
  }

  @Test
  def testCromwellJar(): Unit = {
    System.clearProperty("cromwell.jar")
    intercept[IllegalArgumentException] {
      cromwellJar
    }.getMessage shouldBe "No cromwell jar found, please set the 'cromwell.jar' property"

    val file = File.createTempFile("test.", ".jar")
    System.setProperty("cromwell.jar", file.getAbsolutePath)
    cromwellJar shouldBe file

    file.delete()
    intercept[IllegalArgumentException] {
      cromwellJar
    }.getMessage shouldBe s"requirement failed: Cromwell jar '${file.getAbsoluteFile}' does not exist"
  }

  @Test
  def testCromwellConfig(): Unit = {
    System.clearProperty("cromwell.config")
    cromwellConfig shouldBe None

    val file = File.createTempFile("test.", ".jar")
    System.setProperty("cromwell.config", file.getAbsolutePath)
    cromwellConfig shouldBe Some(file)

    file.delete()
    intercept[IllegalArgumentException] {
      cromwellConfig
    }.getMessage shouldBe s"requirement failed: Cromwell config '${file.getAbsoluteFile}' does not exist"
  }

}
