package nl.biopet.utils.biowdl

import java.io.{File, PrintWriter}

import nl.biopet.utils.io.writeLinesToFile
import nl.biopet.test.BiopetTest
import nl.biopet.utils.conversions
import org.testng.annotations.{BeforeClass, DataProvider, Test}
import play.api.libs.json.Json

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.io.Source
import scala.sys.process.{Process, ProcessLogger}
import scala.util.matching.Regex

trait Pipeline extends BiopetTest {
  /** This can be overwritten by the pipeline */
  def inputs: Map[String, String] = Map()

  def logFile: File = new File(outputDir, "log.out")

  /** Output dir of pipeline */
  lazy val outputDir =
    new File(globalOutputDir, this.getClass.getName)

  @BeforeClass
  def runPipeline(): Unit = {
    val inputsFile = Pipeline.writeInputs(outputDir, inputs)

    val cmd: Seq[String] = Seq("java", "-jar", cromwellJar.getAbsolutePath, "run",
      "-i", inputsFile.getAbsolutePath) ++ cromwellConfig.toList.flatMap(c => Seq("-c", c.getAbsolutePath))

    if (!outputDir.exists()) outputDir.mkdirs()
    if (logFile.exists()) logFile.delete()
    val future = Future {
      val writer = new PrintWriter(logFile)

      def writeLine(line: String): Unit = {
        writer.println(line)
        writer.flush()
      }

      val process = Process(cmd, outputDir).run(ProcessLogger(line => writeLine(line)))
      _exitValue = Some(process.exitValue())
      writer.close()
    }
    Await.result(future, Duration.Inf)
  }

  private var _exitValue: Option[Int] = None

  /** exitvalue of the pipeline, if this is -1 the pipeline is not executed yet */
  def exitValue: Option[Int] = _exitValue

  private var mustHaveFiles: List[File] = Nil
  def addMustHaveFile(path: String*): Unit =
    mustHaveFiles ::= new File(outputDir, path.mkString(File.separator))

  @DataProvider(name = "must_have_files")
  def mustHaveFilesProvider: Array[Array[File]] = mustHaveFiles.map(Array(_)).toArray

  @Test(dataProvider = "must_have_files", dependsOnGroups = Array("parseLog"))
  def testMustHaveFile(file: File): Unit = withClue(s"file: $file") {
    file should exist
  }

  private var mustNotHaveFiles: List[File] = Nil
  def addMustNotHaveFile(path: String*): Unit =
    mustNotHaveFiles ::= new File(outputDir, path.mkString(File.separator))

  @DataProvider(name = "must_not_have_files")
  def mustNotHaveFilesProvider: Array[Array[File]] = mustNotHaveFiles.map(Array(_)).toArray

  @Test(dataProvider = "must_not_have_files", dependsOnGroups = Array("parseLog"))
  def testMustNotHaveFile(file: File): Unit = withClue(s"file: $file") {
    assert(!file.exists())
  }

  def addConditionalFile(condition: Boolean, path: String*): Unit = {
    if (condition) mustHaveFiles ::= new File(outputDir, path.mkString(File.separator))
    else mustNotHaveFiles ::= new File(outputDir, path.mkString(File.separator))
  }

  @Test(priority = -1) def exitcode(): Unit = exitValue shouldBe 0
  @Test def outputDirExist(): Unit = assert(outputDir.exists())

  private var _logLines: List[String] = _
  def logLines: List[String] = _logLines

  @Test(groups = Array("parseLog"))
  def logFileExist(): Unit = {
    assert(logFile.exists())
    _logLines = Source.fromFile(logFile).getLines().toList
  }

  private var logMustHave: List[Regex] = Nil
  def logMustHave(r: Regex): Unit = logMustHave :+= r

  @DataProvider(name = "log_must_have")
  def logMustHaveProvider: Array[Array[Regex]] = logMustHave.map(Array(_)).toArray

  @Test(dataProvider = "log_must_have", dependsOnGroups = Array("parseLog"))
  def testLogMustHave(r: Regex): Unit = withClue(s"regex: $r") {
    assert(logLines.exists(r.findFirstMatchIn(_).isDefined), s"Logfile does not contains: $r")
  }

  private var logMustNotHave: List[Regex] = Nil
  def logMustNotHave(r: Regex): Unit = logMustNotHave :+= r

  @DataProvider(name = "log_must_not_have")
  def logMustNotHaveProvider: Array[Array[Regex]] = logMustNotHave.map(Array(_)).toArray

  @Test(dataProvider = "log_must_not_have", dependsOnGroups = Array("parseLog"))
  def testLogMustNotHave(r: Regex): Unit = withClue(s"regex: $r") {
    val i = logLines.indexWhere(r.findFirstMatchIn(_).isDefined)
    assert(i == -1, s"at line number ${i + 1} in logfile does contains: $r")
  }
}

object Pipeline {
  def writeInputs(outputDir: File, inputs: Map[String, String]): File = {
    val outputFile = new File(outputDir, "inputs.json")
    writeLinesToFile(outputFile, List(Json.stringify(conversions.mapToJson(inputs))))

    outputFile
  }
}
