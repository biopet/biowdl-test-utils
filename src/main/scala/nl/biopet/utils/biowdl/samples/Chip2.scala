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

package nl.biopet.utils.biowdl.samples

import nl.biopet.utils.biowdl.fixtureFile
import nl.biopet.utils.biowdl.multisample.{MultisamplePipeline, Sample}

trait Chip2SingleEnd extends MultisamplePipeline {
  override def samples: Map[String, Sample] =
    addReadgroup(
      super.samples,
      "Chip2",
      "lib1",
      "rg1",
      Map("R1" -> fixtureFile("samples", "chip2", "sample2_R1.fastq.gz"),
          "R1_md5" -> "cf07fa018c049a0ca97f91f0e5b958a2")
    )
}

trait Chip2PairedEnd extends Chip2SingleEnd {
  override def samples: Map[String, Sample] =
    addReadgroup(
      super.samples,
      "Chip2",
      "lib1",
      "rg1",
      Map("R2" -> fixtureFile("samples", "chip2", "sample2_R2.fastq.gz"),
          "R2_md5" -> "74d30ded0d64bcb60ccbff3a390453b4")
    )
}

trait Control2SingleEnd extends MultisamplePipeline {
  override def samples: Map[String, Sample] =
    addReadgroup(
      super.samples,
      "Control2",
      "lib1",
      "rg1",
      Map("R1" -> fixtureFile("samples", "chip2", "sampleCon_R1.fastq.gz"),
          "R1_md5" -> "8e475aa9239be5188f4850e989765dc3")
    )
}

trait Control2PairedEnd extends Control2SingleEnd {
  override def samples: Map[String, Sample] =
    addReadgroup(
      super.samples,
      "Control2",
      "lib1",
      "rg1",
      Map("R2" -> fixtureFile("samples", "chip2", "sampleCon_R2.fastq.gz"),
          "R2_md5" -> "a51c245c4e8790695d5299728e07360e")
    )
}