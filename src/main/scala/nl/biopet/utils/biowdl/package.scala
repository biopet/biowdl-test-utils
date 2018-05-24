package nl.biopet.utils

import java.io.File

package object biowdl {
  lazy val cromwellJar: File = {
    val file = Option(System.getProperties.getProperty("cromwell.jar")).map(new File(_))
      .getOrElse(throw new IllegalArgumentException("No cromwell jar found, please set the 'cromwell.jar' property"))
    require(file.exists(), s"Cromwell jar '$file' does not exist")
    file
  }

  lazy val cromwellConfig: Option[File] = {
    val file = Option(System.getProperties.getProperty("cromwell.config")).map(new File(_))
    file.foreach(f => require(f.exists(), s"Cromwell config '$file' does not exist"))
    file
  }

  lazy val speciesDir: Option[File] = {
    Option(System.getProperties.getProperty("species.dir")).map(new File(_))
  }

  lazy val globalOutputDir: File = {
    Option(System.getProperties.getProperty("biowdl.output_dir")).map(new File(_))
      .getOrElse(throw new IllegalArgumentException("No output_dir found, please set the 'biowdl.output_dir' property"))
  }

  lazy val functionalTests: Boolean =
    Option(System.getProperties.getProperty("biowdl.functionalTests")).exists(_ != "false")

  lazy val integrationTests: Boolean =
    !Option(System.getProperties.getProperty("biowdl.integrationTests")).contains("false")

  lazy val fixtureDir: File = {
    val dir = Option(System.getProperties.getProperty("biowdl.fixture_dir")).map(new File(_))
      .getOrElse(throw new IllegalArgumentException("No output_dir found, please set the 'biowdl.fixture_dir' property"))
    require(dir.exists(), s"Fixture directory does not exist: $dir")
    require(dir.isDirectory, s"Fixture directory is not a directory: $dir")
    dir
  }

  def fixtureFile(paths: String*): File = {
    val file = new File(fixtureDir, paths.mkString(File.separator))
    require(file.exists(), "Fixture file does not exist: " + file)
    require(file.canRead, "Fixture file is not readable: " + file)
    file.getAbsoluteFile
  }
}
