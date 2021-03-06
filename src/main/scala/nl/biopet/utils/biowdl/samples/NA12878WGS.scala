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

trait NA12878WGSSingleEnd
    extends NA12878WGSSingleEndL001
    with NA12878WGSSingleEndL002
trait NA12878WGSPairedEnd
    extends NA12878WGSPairedEndL001
    with NA12878WGSPairedEndL002

trait NA12878WGSSingleEndL001 extends MultisamplePipeline {
  override def samples: Map[String, Sample] = addReadgroup(
    super.samples,
    "NA12878",
    "U0a",
    "L001",
    Map(
      "reads" -> Map(
        "R1" -> fixtureFile("samples",
                            "NA12878",
                            "Sample_U0a",
                            "U0a_CGATGT_L001_R1.fastq.gz"),
        "R1_md5" -> fixtureFile("samples",
                                "NA12878",
                                "Sample_U0a",
                                "U0a_CGATGT_L001_R1.fastq.gz.md5")
      )
    )
  )
}

trait NA12878WGSSingleEndL002 extends MultisamplePipeline {
  override def samples: Map[String, Sample] = addReadgroup(
    super.samples,
    "NA12878",
    "U0a",
    "L002",
    Map(
      "reads" -> Map(
        "R1" -> fixtureFile("samples",
                            "NA12878",
                            "Sample_U0a",
                            "U0a_CGATGT_L002_R1.fastq.gz"),
        "R1_md5" -> fixtureFile("samples",
                                "NA12878",
                                "Sample_U0a",
                                "U0a_CGATGT_L002_R1.fastq.gz.md5")
      )
    )
  )
}

trait NA12878WGSPairedEndL001 extends NA12878WGSSingleEndL001 {
  override def samples: Map[String, Sample] = addReadgroup(
    super.samples,
    "NA12878",
    "U0a",
    "L001",
    Map(
      "reads" -> Map(
        "R2" -> fixtureFile("samples",
                            "NA12878",
                            "Sample_U0a",
                            "U0a_CGATGT_L001_R2.fastq.gz"),
        "R2_md5" -> fixtureFile("samples",
                                "NA12878",
                                "Sample_U0a",
                                "U0a_CGATGT_L001_R2.fastq.gz.md5")
      )
    )
  )
}

trait NA12878WGSPairedEndL002 extends NA12878WGSSingleEndL002 {
  override def samples: Map[String, Sample] = addReadgroup(
    super.samples,
    "NA12878",
    "U0a",
    "L002",
    Map(
      "reads" -> Map(
        "R2" -> fixtureFile("samples",
                            "NA12878",
                            "Sample_U0a",
                            "U0a_CGATGT_L002_R2.fastq.gz"),
        "R2_md5" -> fixtureFile("samples",
                                "NA12878",
                                "Sample_U0a",
                                "U0a_CGATGT_L002_R2.fastq.gz.md5")
      )
    )
  )
}
