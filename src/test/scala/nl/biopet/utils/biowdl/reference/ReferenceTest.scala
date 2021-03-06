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

package nl.biopet.utils.biowdl.reference

import java.io.File

import nl.biopet.test.BiopetTest
import nl.biopet.utils.biowdl.references.Reference
import org.testng.annotations.Test

class ReferenceTest extends BiopetTest {
  @Test
  def testBwaNonAlt(): Unit = {
    val nonAlt: Reference = new Reference {
      def referenceSpecies: String = "test"
      def referenceName: String = "test"
      def referenceFasta: File = ???
      override def bwaMemFasta = Some(new File("fasta"))
      override def bwaMemIndexFiles = List(new File("index"))
    }

    nonAlt.bwaIndexInputs shouldBe Map(
      "fastaFile" -> new File("fasta").getAbsolutePath,
      "indexFiles" -> List(new File("index").getAbsolutePath))
  }

  @Test
  def testBwaAlt(): Unit = {
    val nonAlt: Reference = new Reference {
      def referenceSpecies: String = "test"
      def referenceName: String = "test"
      def referenceFasta: File = ???
      override def bwaMemFasta = Some(new File("fasta"))
      override def bwaMemIndexFiles = List(new File("index"))
      override def bwaMemAlt = Some(new File("alt"))
    }

    nonAlt.bwaIndexInputs shouldBe Map(
      "fastaFile" -> new File("fasta").getAbsolutePath,
      "indexFiles" -> List(new File("index").getAbsolutePath),
      "altIndex" -> new File("alt").getAbsolutePath
    )
  }

}
