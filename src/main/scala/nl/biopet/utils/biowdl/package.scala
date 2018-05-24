package nl.biopet.utils

import java.io.File

package object biowdl {
  lazy val cromwellJar: File = {
    System.getProperties.getProperty("cromwell.jar") match {
      case s: String => {
        val file = new File(s)
        if (!file.exists())
          throw new IllegalArgumentException("Cromwell jar '" + file + "' does not exist")
        file
      }
      case _ =>
        throw new IllegalArgumentException(
          "No cromwell jar found, please set the 'cromwell.jar' property")
    }
  }

  lazy val cromwellConfig: File = {
    System.getProperties.getProperty("cromwell.config") match {
      case s: String => {
        val file = new File(s)
        if (!file.exists())
          throw new IllegalArgumentException("Cromwell config '" + file + "' does not exist")
        file
      }
      case _ =>
        throw new IllegalArgumentException(
          "No cromwell config found, please set the 'cromwell.config' property")
    }
  }

  lazy val speciesDir: Option[File] = {
    System.getProperties.getProperty("species.dir") match {
      case path: String => Some(new File(path))
      case _ => None
    }
  }

  lazy val globalOutputDir: File = {
    System.getProperties.getProperty("biowdl.output_dir") match {
      case s: String => new File(s)
      case _ =>
        throw new IllegalArgumentException(
          "No output_dir found, please set the 'biowdl.output_dir' property")
    }
  }


  lazy val functionalTests: Boolean = {
    System.getProperties.getProperty("biowdl.functionalTests") match {
      case "false" => false
      case null => false
      case _ => true
    }
  }

  lazy val integrationTests: Boolean = {
    System.getProperties.getProperty("biowdl.integrationTests") match {
      case "false" => false
      case null => true
      case _ => true
    }
  }

  lazy val fixtureDir: File = {
    System.getProperties.getProperty("biowdl.fixture_dir") match {
      case s: String => {
        val dir = new File(s)
        require(dir.exists(), "Fixture directory does not exist: " + s)
        require(dir.isDirectory, "Fixture directory is not a directory: " + s)
        dir
      }
      case _ =>
        throw new IllegalArgumentException(
          "No output_dir found, please set the 'biowdl.fixture_dir' property")
    }
  }

  def fixtureFile(paths: String*): File = {
    val file = new File(fixtureDir, paths.mkString(File.separator))
    require(file.exists(), "Fixture file does not exist: " + file)
    require(file.canRead, "Fixture file is not readable: " + file)
    file.getAbsoluteFile
  }
}
