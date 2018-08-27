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
  def cromwellJar: File = {
    val file = Option(System.getProperties.getProperty("cromwell.jar"))
      .map(new File(_))
      .getOrElse(throw new IllegalArgumentException(
        "No cromwell jar found, please set the 'cromwell.jar' property"))
    require(file.exists(), s"Cromwell jar '$file' does not exist")
    file
  }

  def cromwellConfig: Option[File] = {
    val file = Option(System.getProperties.getProperty("cromwell.config"))
      .map(new File(_))
    file.foreach(
      f =>
        require(f.exists(),
                s"Cromwell config '${file.getOrElse("")}' does not exist"))
    file
  }

  def cromwellExtraOptions: Seq[String] = {
    val extraOptions = Option(
      System.getProperties.getProperty("cromwell.extraOptions"))
    extraOptions.map(_.split(",").toSeq).getOrElse(Seq[String]())
  }

  def speciesDir: Option[File] = {
    Option(System.getProperties.getProperty("biowdl.species.dir"))
      .map(new File(_))
  }

  def globalOutputDir: File = {
    Option(System.getProperties.getProperty("biowdl.outputDir"))
      .map(new File(_))
      .getOrElse(throw new IllegalArgumentException(
        "No output_dir found, please set the 'biowdl.outputDir' property"))
  }

  def functionalTests: Boolean = {
    Option(System.getProperties.getProperty("biowdl.functionalTests")) match {
      case Some("false") => false
      case Some("true")  => true
      case Some(x) =>
        throw new IllegalArgumentException(
          s"for 'biowdl.functionalTests' only true or false is allowed, found: $x")
      case _ => false
    }
  }

  def integrationTests: Boolean =
    Option(System.getProperties.getProperty("biowdl.integrationTests")) match {
      case Some("false") => false
      case Some("true")  => true
      case Some(x) =>
        throw new IllegalArgumentException(
          s"for 'biowdl.integrationTests' only true or false is allowed, found: $x")
      case _ => true
    }

  def fixtureDir: File = {
    val dir = Option(System.getProperties.getProperty("biowdl.fixtureDir"))
      .map(new File(_))
      .getOrElse(throw new IllegalArgumentException(
        "No output_dir found, please set the 'biowdl.fixtureDir' property"))
    require(dir.exists(), s"Fixture directory does not exist: $dir")
    require(dir.isDirectory, s"Fixture directory is not a directory: $dir")
    dir
  }

  def zipped: Boolean =
    Option(System.getProperties.getProperty("biowdl.zipped")) match {
      case Some("false") => false
      case Some("true")  => true
      case Some(x) =>
        throw new IllegalArgumentException(
          s"for 'biowdl.zipped' only true or false is allowed, found: $x")
      case _ => false
    }

  def threads: Int = {
    Option(System.getProperties.getProperty("biowdl.threads"))
      .map(_.toInt)
      .getOrElse(1)
  }

  implicit val executionContext: ExecutionContext =
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
