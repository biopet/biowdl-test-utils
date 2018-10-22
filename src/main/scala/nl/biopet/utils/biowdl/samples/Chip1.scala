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

trait Chip1SingleEnd extends MultisamplePipeline {
  override def samples: Map[String, Sample] =
    addReadgroup(
      super.samples,
      "Chip1",
      "lib1",
      "rg1",
      Map("reads" -> Map(
        "R1" -> fixtureFile("samples", "chip1", "sample1_R1.fastq.gz"),
        "R1_md5" -> fixtureFile("samples", "chip1", "sample1_R1.fastq.gz.md5")))
    )
}

trait Chip1PairedEnd extends Chip1SingleEnd {
  override def samples: Map[String, Sample] =
    addReadgroup(
      super.samples,
      "Chip1",
      "lib1",
      "rg1",
      Map("reads" -> Map(
        "R2" -> fixtureFile("samples", "chip1", "sample1_R2.fastq.gz"),
        "R2_md5" -> fixtureFile("samples", "chip1", "sample1_R2.fastq.gz.md5")))
    )
}

trait Control1SingleEnd extends MultisamplePipeline {
  override def samples: Map[String, Sample] =
    addReadgroup(
      super.samples,
      "Control1",
      "lib2",
      "rg1",
      Map(
        "reads" -> Map(
          "R1" -> fixtureFile("samples", "chip1", "sampleCon_R1.fastq.gz"),
          "R1_md5" -> fixtureFile("samples",
                                  "chip1",
                                  "sampleCon_R1.fastq.gz.md5")))
    )
}

trait Control1PairedEnd extends Control1SingleEnd {
  override def samples: Map[String, Sample] =
    addReadgroup(
      super.samples,
      "Control1",
      "lib2",
      "rg1",
      Map(
        "reads" -> Map(
          "R2" -> fixtureFile("samples", "chip1", "sampleCon_R2_fastq.gz"),
          "R2_md5" -> fixtureFile("samples",
                                  "chip1",
                                  "sampleCon_R2_fastq.gz.md5")))
    )
}
