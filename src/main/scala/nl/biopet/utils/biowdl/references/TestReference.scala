package nl.biopet.utils.biowdl.references

import java.io.File

import nl.biopet.utils.biowdl.{fixtureFile, fixtureDir}

/**
  * Created by pjvanthof on 14/11/15.
  */
trait TestReference extends Reference {
  def referenceSpecies = "test"
  def referenceName = "test"
  def referenceFasta: File = fixtureFile("reference/reference.fasta")

  override def bwaMemFasta = Some(fixtureFile("reference/bwa/reference.fasta"))

  override def bowtieIndex: Option[File] =
    Some(fixtureFile("reference/bowtie/reference"))

  override def bowtie2Index: Option[File] =
    Some(fixtureFile("reference/bowtie2/reference"))

  override def tophatIndex: Option[String] =
    Some(fixtureDir + "reference/bowtie2/reference")

  override def gsnapDir: Option[File] = Some(fixtureFile("reference/gmap"))

  override def gsnapDb: Option[String] = Some("reference")

  override def starGenomeDir: Option[File] = Some(fixtureFile("reference/star"))

  override def hisat2Index: Option[String] =
    Some(fixtureDir + "reference/hisat2/reference")
}
