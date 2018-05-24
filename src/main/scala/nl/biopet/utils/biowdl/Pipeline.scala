package nl.biopet.utils.biowdl

import java.io.File

import nl.biopet.test.BiopetTest
import nl.biopet.utils.conversions
import org.testng.annotations.BeforeClass

trait Pipeline extends BiopetTest {
  /** This can be overwritten by the pipeline */
  def inputs: Map[String, String] = Map()

  /** Output dir of pipeline */
  lazy val outputDir =
    new File(globalOutputDir, this.getClass.getName)

  @BeforeClass
  def runPipeline(): Unit = {
    val inputsFile = Pipeline.writeInputs(outputDir, inputs)

    val cmd: Seq[String] = Seq("java", "-jar", cromwellJar.getAbsolutePath, "run",
      "-c", cromwellConfig.getAbsolutePath,
      "-i", inputsFile.getAbsolutePath)

    //TODO: run cromwell
  }

  private var _exitValue: Option[Int] = None

  /** exitvalue of the pipeline, if this is -1 the pipeline is not executed yet */
  def exitValue: Option[Int] = _exitValue

}

object Pipeline {
  def writeInputs(outputDir: File, inputs: Map[String, String]): File = {
    val outputFile = new File(outputDir, "inputs.json")

    val json = conversions.mapToJson(inputs)
    //TODO: write inputs

    outputFile
  }
}
