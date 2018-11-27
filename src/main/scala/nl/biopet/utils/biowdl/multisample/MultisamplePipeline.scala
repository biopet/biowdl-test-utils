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
import java.util.NoSuchElementException

import nl.biopet.utils.biowdl.Pipeline
import nl.biopet.utils.conversions.mapToYamlFile
import nl.biopet.utils.conversions.mergeMaps
import org.testng.annotations.{DataProvider, Test}

trait MultisamplePipeline extends Pipeline {
  def samples: List[Sample] = List()

  def sampleDir(sample: Sample): File = sampleDir(sample.name)
  def sampleDir(sample: String) =
    new File(outputDir, "samples" + File.separator + sample)
  def libraryDir(library: Library): File =
    libraryDir(library.sample, library.name)
  def libraryDir(sample: String, library: String) =
    new File(sampleDir(sample), s"lib_$library")
  def readgroupDir(readgroup: Readgroup): File =
    readgroupDir(readgroup.sample, readgroup.library, readgroup.name)
  def readgroupDir(sample: String, library: String, readgroup: String) =
    new File(libraryDir(sample, library), s"rg_$readgroup")

  val sampleConfig: Map[String, Any] = Map("samples" -> samples.map(_.toMap))
  def sampleConfigFile = new File(outputDir, "samples.yml")

  override def inputs: Map[String, Any] = {
    mapToYamlFile(sampleConfig, sampleConfigFile)
    super.inputs + (s"$startPipelineName.sampleConfigFiles" -> Array(
      sampleConfigFile.getAbsolutePath))
  }

  @DataProvider(name = "samples")
  def sampleProvider: Array[Array[Sample]] =
    samples.map(Array(_)).toArray

  @DataProvider(name = "libraries")
  def libraryProvider: Array[Array[Library]] =
    samples.flatMap(_.libraries).map(Array(_)).toArray

  @DataProvider(name = "readgroups")
  def readgroupProvider: Array[Array[Readgroup]] =
    samples
      .flatMap(_.libraries.flatMap(_.readgroups))
      .map(Array(_))
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

  def addSample(current: List[Sample],
                sample: String,
                config: Map[String, Any] = Map(),
                libraries: List[Library] = List()): List[Sample] = {
    current.indexWhere(_.name == sample) match {
      // -1 -> item was not found in sample list
      case -1 => current ++ List(Sample(sample, config))
      // else -> it was found on some index
      case x =>
        current.drop(x) ++ List(
          Sample(sample,
                 mergeMaps(current(x).config, config),
                 current(x).libraries ++ libraries))
    }
  }

  def addLibrary(current: List[Sample],
                 sample: String,
                 library: String,
                 config: Map[String, Any] = Map()): List[Sample] = {
    val withSample = addSample(current, sample)
    val addedSampleIndex: Int = withSample.indexWhere(_.name == sample) match {
      case -1     => throw new NoSuchElementException
      case x: Int => x
    }
    val lib =
      current(addedSampleIndex).libraries.indexWhere(_.name == library) match {
        case -1 => Library(sample, library, config)
        case x: Int => {
          val currentLib = current(addedSampleIndex).libraries(x)
          currentLib.copy(config = mergeMaps(currentLib.config, config))
        }
      }
    withSample.drop(addedSampleIndex) ++ List(
      withSample(addedSampleIndex).copy(
        libraries = withSample(addedSampleIndex).libraries ++ List(lib)))
  }

  def addReadgroup(current: List[Sample],
                   sample: String,
                   library: String,
                   readgroup: String,
                   config: Map[String, Any] = Map()): List[Sample]= {
    val withLibrary = addLibrary(current, sample, library)
    val sampleIndex = withLibrary.indexWhere(_.name == sample) match {
      case -1 => throw NoSuchElementException
      case x => x
    }
    val currentSample = withLibrary(sampleIndex)
    val libIndex = currentSample.libraries.indexWhere(_.name == library)
    val currentLib = currentSample.libraries(libIndex)
    val newRgs: List[Readgroup] = currentLib.readgroups.indexWhere(_.name = readgroup) match {
      case -1 => currentLib.readgroups ++ List(Readgroup(sample, library, readgroup, config))
      case x: Int => {
        val currentRg = currentLib.readgroups(x)
        val modRg = currentRg.copy(config = mergeMaps(currentRg.config, config))
        currentLib.readgroups.drop(x) ++ List(modRg)
      }
    }
    val newLib = Library(name = library, sample = sample, config = currentLib.config, readgroups = newRgs)
    withLibrary.drop(sampleIndex) ++ List(Sample(name = sample, config = currentSample.config, libraries = currentSample.libraries ++ ))
    val rg = l.readgroups.get(readgroup) match {
      case Some(r) => r.copy(config = mergeMaps(r.config, config))
      case _       => Readgroup(sample, library, readgroup, config)
    }
    withLibrary + (sample -> s.copy(
      libraries = s.libraries + (library -> l.copy(
        readgroups = l.readgroups + (readgroup -> rg)))))
  }
}
