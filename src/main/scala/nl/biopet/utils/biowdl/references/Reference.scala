package nl.biopet.utils.biowdl.references

import java.io.File

trait Reference {
  def referenceSpecies: String

  def referenceName: String

  def referenceFasta: File

  def referenceFastaIndexFile =
    new File(referenceFasta.getAbsolutePath + ".fai")
  def referenceFastaDictFile =
    new File(
      referenceFasta.getAbsolutePath
        .stripSuffix(".fa")
        .stripSuffix(".fna")
        .stripSuffix(".fasta") + ".dict")

  def bwaMemFasta: Option[File] = None

  def bowtieIndex: Option[File] = None
  def bowtie2Index: Option[File] = None
  def tophatIndex: Option[String] = None
  def gsnapDir: Option[File] = None
  def gsnapDb: Option[String] = None
  def starGenomeDir: Option[File] = None
  def hisat2Index: Option[String] = None

}
