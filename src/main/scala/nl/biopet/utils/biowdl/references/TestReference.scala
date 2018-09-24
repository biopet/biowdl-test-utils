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

import nl.biopet.utils.biowdl.{fixtureFile, fixtureDir}

/**
  * Created by pjvanthof on 14/11/15.
  */
trait TestReference extends Reference {
  def referenceSpecies = "test"
  def referenceName = "test"
  def referenceFasta: File = fixtureFile("references/test/reference/reference.fasta")

  override def bwaMemFasta = Some(fixtureFile("references/test/reference/bwa/reference.fasta"))

  override def bowtieIndex: Option[File] =
    Some(fixtureFile("references/test/reference/bowtie/reference"))

  override def bowtie2Index: Option[File] =
    Some(fixtureFile("references/test/reference/bowtie2/reference"))

  override def tophatIndex: Option[String] =
    Some(fixtureDir + "references/test/reference/bowtie2/reference")

  override def gsnapDir: Option[File] = Some(fixtureFile("references/test/reference/gmap"))

  override def gsnapDb: Option[String] = Some("reference")

  override def starGenomeDir: Option[File] = Some(fixtureFile("references/test/reference/star"))

  override def hisat2Index: Option[String] =
    Some(fixtureDir + "references/test/reference/hisat2/reference")
}
