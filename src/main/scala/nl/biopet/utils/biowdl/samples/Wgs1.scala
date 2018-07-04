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

trait Wgs1SingleEnd extends MultisamplePipeline {
  override def samples: Map[String, Sample] =
    addReadgroup(super.samples,
                 "wgs1",
                 "lib1",
                 "rg1",
                 Map("R1" -> fixtureFile("samples", "wgs1", "R1.fq.gz"),
                     "R1_md5" -> "b859d6dd76a6861ce7e9a978ae2e530e"))
}

trait Wgs1PairedEnd extends ChIP1SingleEnd {
  override def samples: Map[String, Sample] =
    addReadgroup(super.samples,
                 "wgs1",
                 "lib1",
                 "rg1",
                 Map("R2" -> fixtureFile("samples", "wgs1", "R2.fq.gz"),
                     "R2_md5" -> "986acc7bda0bf2ef55c52431f54fe3a9"))
}
