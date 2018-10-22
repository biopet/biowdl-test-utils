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

import java.io.{File, PrintWriter}

import nl.biopet.utils.io.writeLinesToFile
import nl.biopet.utils.io.listDirectory
import nl.biopet.test.BiopetTest
import nl.biopet.utils.{Logging, conversions}
import org.testng.annotations.{BeforeClass, DataProvider, Test}
import play.api.libs.json.Json
import org.apache.commons.io.FileUtils.deleteDirectory
import org.testng.SkipException

import scala.collection.mutable.ListBuffer
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.io.Source
import scala.sys.process.{Process, ProcessLogger}
import scala.util.matching.Regex

trait Pipeline extends BiopetTest with Logging {

  /** File to start the pipeline from */
  def startFile: File

  /** Name of the pipeline inside the wdl file, will be used for configs */
  def startPipelineName: String = startFile.getName.stripSuffix(".wdl")

  def backend: String = cromwellBackend

  /** This can be overwritten by the pipeline */
  def inputs: Map[String, Any] = Map()

  def logFile: File = new File(outputDir, "log.out")
  def rerunLogFile: File = new File(outputDir, "rerun.log.out")

  /** Output dir of pipeline */
  def outputDir =
    new File(globalOutputDir, this.getClass.getName)

  def createImportsZip: Boolean = zipped

  private def runPipeline(rerun: Boolean = false): Unit = {
    val javaCmd = Seq(
      "java",
      s"-Dbackend.providers.$backend.config.root=${outputDir.getAbsolutePath}/.cromwell-executions") ++
      cromwellConfig
        .map(c => s"-Dconfig.file=${c.getAbsolutePath}")
        .toSeq ++
      cromwellExtraOptions

    val cmd: Seq[String] = javaCmd ++ Seq("-jar",
                                          cromwellJar.getAbsolutePath,
                                          "run",
                                          "-i",
                                          inputsFile.getAbsolutePath) ++
      createImportZip.toSeq.flatMap(x => List("--imports", x.getAbsolutePath)) ++
      Seq(startFile.getAbsolutePath)

    val future = Future {
      val writer = new PrintWriter(if (rerun) rerunLogFile else logFile)

      def writeLine(line: String): Unit = {
        writer.println(line)
        writer.flush()
      }
      logger.info(s"Start command: ${cmd.mkString(" ")}")
      val process =
        // startFile.getParentFile -> Run in root directory of WDL pipeline file because of imports issue
        Process(cmd, cwd = startFile.getParentFile).run(ProcessLogger(line =>
          writeLine(line)))
      _exitValue = Some(process.exitValue())
      writer.close()
    }(executionContext)
    Await.result(future, Duration.Inf)
    logger.info(s"Done command: ${cmd.mkString(" ")}")
  }

  lazy private val createImportZip: Option[File] = {
    if (createImportsZip) {
      val files =
        listDirectory(new File("."), recursive = true)
          .filter(_.getName.endsWith(".wdl"))
      val zipFile = new File(outputDir, "imports.zip")
      val zipCmd = Seq("zip", zipFile.toString) ++ files.map(_.toString)
      val p = Process(zipCmd, cwd = startFile.getParentFile).run()
      require(p.exitValue() == 0)
      Some(zipFile)
    } else None
  }

  lazy private val inputsFile: File = Pipeline.writeInputs(outputDir, inputs)

  @BeforeClass
  def run(): Unit = {
    if (functionalTest && !functionalTests)
      throw new SkipException("Functional tests are disabled")
    if (!integrationTests && !functionalTest)
      throw new SkipException("Integration tests are disabled")

    if (outputDir.exists()) {
      deleteDirectory(outputDir)
    }
    outputDir.mkdirs()

    runPipeline()
  }

  private var _exitValue: Option[Int] = None

  /** exitvalue of the pipeline, if this is -1 the pipeline is not executed yet */
  def exitValue: Option[Int] = _exitValue

  @Test(priority = -1, groups = Array("exitcode"))
  def exitcode(): Unit = exitValue shouldBe Some(0)

  @Test def outputDirExist(): Unit = {
    assert(outputDir.exists())
    assert(outputDir.isDirectory)
  }

  @Test(dependsOnGroups = Array("exitcode"))
  def testCallCaching(): Unit = {
    runPipeline(true)
    exitValue shouldBe Some(0)
    val reader = Source.fromFile(rerunLogFile)
    val t = reader.getLines().exists(_.contains("")) //TODO: check text to grep
    reader.close()
    assert(!t, "Jobs did rerun, should not happen")
  }

  private val mustHaveFiles: ListBuffer[File] = ListBuffer()
  def addMustHaveFile(path: String*): Unit =
    mustHaveFiles.append(new File(outputDir, path.mkString(File.separator)))
  def addMustHaveFile(file: File): Unit = mustHaveFiles.append(file)

  @DataProvider(name = "must_have_files")
  def mustHaveFilesProvider: Array[Array[File]] =
    mustHaveFiles.map(Array(_)).toArray

  @Test(dataProvider = "must_have_files", dependsOnGroups = Array("parseLog"))
  def testMustHaveFile(file: File): Unit = withClue(s"file: $file") {
    file should exist
  }

  private val mustNotHaveFiles: ListBuffer[File] = ListBuffer()
  def addMustNotHaveFile(path: String*): Unit =
    mustNotHaveFiles.append(new File(outputDir, path.mkString(File.separator)))
  def addMustNotHaveFile(file: File): Unit = mustNotHaveFiles.append(file)

  @DataProvider(name = "must_not_have_files")
  def mustNotHaveFilesProvider: Array[Array[File]] =
    mustNotHaveFiles.map(Array(_)).toArray

  @Test(dataProvider = "must_not_have_files",
        dependsOnGroups = Array("parseLog"))
  def testMustNotHaveFile(file: File): Unit = withClue(s"file: $file") {
    assert(!file.exists())
  }

  def addConditionalFile(condition: Boolean, path: String*): Unit = {
    if (condition)
      mustHaveFiles.append(new File(outputDir, path.mkString(File.separator)))
    else
      mustNotHaveFiles.append(
        new File(outputDir, path.mkString(File.separator)))
  }

  private var _logLines: List[String] = _
  def logLines: List[String] = _logLines

  @Test(groups = Array("parseLog"))
  def logFileExist(): Unit = {
    assert(logFile.exists())
    _logLines = Source.fromFile(logFile).getLines().toList
  }

  private val logMustHave: ListBuffer[Regex] = ListBuffer()
  def logMustHave(r: Regex): Unit = logMustHave.append(r)

  @DataProvider(name = "log_must_have")
  def logMustHaveProvider: Array[Array[Regex]] =
    logMustHave.map(Array(_)).toArray

  @Test(dataProvider = "log_must_have", dependsOnGroups = Array("parseLog"))
  def testLogMustHave(r: Regex): Unit = withClue(s"regex: $r") {
    assert(logLines.exists(r.findFirstMatchIn(_).isDefined),
           s"Logfile does not contains: $r")
  }

  private val logMustNotHave: ListBuffer[Regex] = ListBuffer()
  def logMustNotHave(r: Regex): Unit = logMustNotHave.append(r)

  @DataProvider(name = "log_must_not_have")
  def logMustNotHaveProvider: Array[Array[Regex]] =
    logMustNotHave.map(Array(_)).toArray

  @Test(dataProvider = "log_must_not_have", dependsOnGroups = Array("parseLog"))
  def testLogMustNotHave(r: Regex): Unit = withClue(s"regex: $r") {
    val i = logLines.indexWhere(r.findFirstMatchIn(_).isDefined)
    assert(i == -1, s"at line number ${i + 1} in logfile does contains: $r")
  }

  /**
    * Create a optional file object and calls `addConditionalFile` directly, even if the condition is false
    * @param condition Condition for the file to be created, if false None is returned
    * @param path Path to the file
    * @return
    */
  def createOptionalFile(condition: Boolean, path: String*): Option[File] = {
    val p = path.mkString(File.separator)
    addConditionalFile(condition, p)
    if (condition) Some(new File(outputDir, p))
    else None
  }

  /**
    * Create a file object and calls `addMustHaveFile` directly
    * @param path Path to the file
    * @return
    */
  def createFile(path: String*): File = {
    val p = path.mkString(File.separator)
    addMustHaveFile(p)
    new File(outputDir, p)
  }

  def functionalTest: Boolean = false
}

object Pipeline {
  def writeInputs(outputDir: File, inputs: Map[String, Any]): File = {
    val outputFile = new File(outputDir, "inputs.json")
    writeLinesToFile(outputFile,
                     List(Json.stringify(conversions.mapToJson(inputs))))

    outputFile
  }
}
