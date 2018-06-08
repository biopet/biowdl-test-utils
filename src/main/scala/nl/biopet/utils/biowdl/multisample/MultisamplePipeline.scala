package nl.biopet.utils.biowdl.multisample

import java.io.File

import nl.biopet.utils.biowdl.Pipeline
import nl.biopet.utils.conversions.mapToYamlFile
import nl.biopet.utils.conversions.mergeMaps
import org.testng.annotations.{DataProvider, Test}

trait MultisamplePipeline extends Pipeline {
  def samples: Map[String, Sample] = Map()

  def sampleDir(sample: Sample): File = sampleDir(sample.name)
  def sampleDir(sample: String) =
    new File(outputDir, "samples" + File.separator + sample)
  def libraryDir(library: Library): File = libraryDir(library.sample, library.library)
  def libraryDir(sample: String, library: String) =
    new File(sampleDir(sample), s"lib_$library")
  def readgroupDir(readgroup: Readgroup): File = readgroupDir(readgroup.sample, readgroup.library, readgroup.readgroup)
  def readgroupDir(sample: String, library: String, readgroup: String) =
    new File(libraryDir(sample, library), s"rg_$readgroup")

  val sampleConfig: Map[String, Any] = samples.map {
    case (name, sample) => name -> sample.toMap
  }
  val sampleConfigFile = new File(outputDir, "sample.yml")

  override def inputs: Map[String, Any] = {
    mapToYamlFile(sampleConfig, sampleConfigFile)
    super.inputs + ("pipeline.sampleConfigs" -> Array(
      sampleConfigFile.getAbsolutePath))
  }

  @DataProvider(name = "samples")
  def sampleProvider: Array[Array[Sample]] =
    samples.values.map(x => Array(x)).toArray

  @DataProvider(name = "libraries")
  def libraryProvider: Array[Array[Library]] =
    samples.flatMap(sample => sample._2.libraries.map(x => Array(x._2))).toArray

  @DataProvider(name = "readgroups")
  def readgroupProvider: Array[Array[Readgroup]] =
    samples
      .flatMap(sample =>
        sample._2.libraries.flatMap(_._2.readgroups.map(x => Array(x._2))))
      .toArray

  @Test(dataProvider = "samples")
  def testSampleDir(sample: Sample): Unit = {
    sampleDir(sample) should exist
    sampleDir(sample).isDirectory shouldBe true
  }

  @Test(dataProvider = "libraries")
  def testLibraryDir(library: Library): Unit = {
    libraryDir(library) should exist
    libraryDir(library).isDirectory shouldBe true
  }

  @Test(dataProvider = "readgroups")
  def testReadgroupDir(readgroup: Readgroup): Unit = {
    readgroupDir(readgroup) should exist
    readgroupDir(readgroup).isDirectory shouldBe true
  }

  def addSample(current: Map[String, Sample],
                sample: String,
                config: Map[String, Any] = Map()): Map[String, Sample] = {
    current.get(sample) match {
      case Some(s) =>
        current + (sample -> s.copy(config = mergeMaps(s.config, config)))
      case _ => current + (sample -> Sample(sample, config))
    }
  }

  def addLibrary(current: Map[String, Sample],
                 sample: String,
                 library: String,
                 config: Map[String, Any] = Map()): Map[String, Sample] = {
    val withSample = addSample(current, sample)
    val s = withSample(sample)
    val lib = s.libraries.get(library) match {
      case Some(l) => l.copy(config = mergeMaps(l.config, config))
      case _       => Library(sample, library, config)
    }
    withSample + (sample -> s.copy(libraries = s.libraries + (library -> lib)))
  }

  def addReadgroup(current: Map[String, Sample],
                   sample: String,
                   library: String,
                   readgroup: String,
                   config: Map[String, Any] = Map()): Map[String, Sample] = {
    val withLibrary = addLibrary(current, sample, library)
    val s = withLibrary(sample)
    val l = s.libraries(library)
    val rg = l.readgroups.get(readgroup) match {
      case Some(r) => r.copy(config = mergeMaps(l.config, config))
      case _       => Readgroup(sample, library, readgroup, config)
    }
    withLibrary + (sample -> s.copy(
      libraries = s.libraries + (library -> l.copy(
        readgroups = l.readgroups + (readgroup -> rg)))))
  }
}
