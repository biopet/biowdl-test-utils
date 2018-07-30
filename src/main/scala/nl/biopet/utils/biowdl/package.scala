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

package nl.biopet.utils

import java.io.File
import java.util.concurrent.Executors

import scala.concurrent.ExecutionContext

package object biowdl {
  lazy val cromwellJar: File = {
    val file = Option(System.getProperties.getProperty("cromwell.jar"))
      .map(new File(_))
      .getOrElse(throw new IllegalArgumentException(
        "No cromwell jar found, please set the 'cromwell.jar' property"))
    require(file.exists(), s"Cromwell jar '$file' does not exist")
    file
  }

  lazy val cromwellConfig: Option[File] = {
    val file = Option(System.getProperties.getProperty("cromwell.config"))
      .map(new File(_))
    file.foreach(f =>
      require(f.exists(), s"Cromwell config '$file' does not exist"))
    file
  }

  lazy val cromwellExtraOptions: Seq[String] = {
    val extraOptions = Option(
      System.getProperties.getProperty("cromwell.extraOptions"))
    extraOptions.map(_.split(",").toSeq).getOrElse(Seq[String]())
  }

  lazy val speciesDir: Option[File] = {
    Option(System.getProperties.getProperty("species.dir")).map(new File(_))
  }

  lazy val globalOutputDir: File = {
    Option(System.getProperties.getProperty("biowdl.outputDir"))
      .map(new File(_))
      .getOrElse(throw new IllegalArgumentException(
        "No output_dir found, please set the 'biowdl.outputDir' property"))
  }

  lazy val functionalTests: Boolean =
    Option(System.getProperties.getProperty("biowdl.functionalTests"))
      .exists(_ != "false")

  lazy val integrationTests: Boolean =
    !Option(System.getProperties.getProperty("biowdl.integrationTests"))
      .contains("false")

  lazy val fixtureDir: File = {
    val dir = Option(System.getProperties.getProperty("biowdl.fixtureDir"))
      .map(new File(_))
      .getOrElse(throw new IllegalArgumentException(
        "No output_dir found, please set the 'biowdl.fixtureDir' property"))
    require(dir.exists(), s"Fixture directory does not exist: $dir")
    require(dir.isDirectory, s"Fixture directory is not a directory: $dir")
    dir
  }

  lazy val threads: Int = {
    Option(System.getProperties.getProperty("biowdl.threads"))
      .map(_.toInt)
      .getOrElse(1)
  }

  implicit lazy val executionContext: ExecutionContext =
    ExecutionContext.fromExecutor(
      ExecutionContext.fromExecutorService(
        Executors.newWorkStealingPool(threads)))

  def fixtureFile(paths: String*): File = {
    val file = new File(fixtureDir, paths.mkString(File.separator))
    require(file.exists(), "Fixture file does not exist: " + file)
    require(file.canRead, "Fixture file is not readable: " + file)
    file.getAbsoluteFile
  }
}
