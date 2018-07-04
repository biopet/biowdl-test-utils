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
      "ChIP1",
      "lib1",
      "rg1",
      Map("R1" -> fixtureFile("samples", "chip1", "sample1_R1.fastq.gz"),
          "R1_md5" -> "d538c6e674b7152cf201a10dd75f08e8"))
}

trait Chip1PairedEnd extends Chip1SingleEnd {
  override def samples: Map[String, Sample] =
    addReadgroup(
      super.samples,
      "ChIP1",
      "lib1",
      "rg1",
      Map("R2" -> fixtureFile("samples", "chip1", "sample1_R2.fastq.gz"),
          "R2_md5" -> "9b662817e93a625642e312f595c1d7e0"))
}
