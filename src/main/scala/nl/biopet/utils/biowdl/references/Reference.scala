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

  /** This returns the input section of a bwa index */
  def bwaIndexInputs: Map[String, Any] = Map(
    "fastaFile" -> bwaMemFasta.getOrElse(throw new IllegalStateException),
    "indexFiles" -> bwaMemIndexFiles.map(_.getAbsolutePath)
  ) ++ bwaMemAlt.map("altIndex" -> _.getAbsolutePath)

  def bwaMemFasta: Option[File] = None
  def bwaMemAlt: Option[File] = None
  def bwaMemIndexFiles: List[File] =
    bwaMemFasta.toList.flatMap(
      x =>
        List(
          new File(x.getAbsolutePath + ".sa"),
          new File(x.getAbsolutePath + ".amb"),
          new File(x.getAbsolutePath + ".ann"),
          new File(x.getAbsolutePath + ".bwt"),
          new File(x.getAbsolutePath + ".pac")
      ))

  def bowtieIndex: Option[File] = None
  def bowtie2Index: Option[File] = None
  def tophatIndex: Option[String] = None
  def gsnapDir: Option[File] = None
  def gsnapDb: Option[String] = None
  def starGenomeDir: Option[File] = None
  def hisat2Index: Option[String] = None
}
