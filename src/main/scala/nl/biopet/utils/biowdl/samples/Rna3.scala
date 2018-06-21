package nl.biopet.utils.biowdl.samples

import nl.biopet.utils.biowdl.fixtureFile
import nl.biopet.utils.biowdl.multisample.{MultisamplePipeline, Sample}

trait Rna3SingleEnd extends MultisamplePipeline {
  override def samples: Map[String, Sample] =
    addReadgroup(super.samples,
                 "rna3",
                 "lib1",
                 "rg1",
                 Map("R1" -> fixtureFile("samples", "rna3", "R1.fq.gz"),
                     "R1_md5" -> "2e53e727dad4cbbfedadeb29404ca0d3"))
}

trait Rna3PairedEnd extends Rna3SingleEnd {
  override def samples: Map[String, Sample] =
    addReadgroup(super.samples,
                 "wgs1",
                 "lib1",
                 "rg1",
                 Map("R2" -> fixtureFile("samples", "rna3", "R2.fq.gz"),
                     "R2_md5" -> "f3e4227bb7ab4028f4f146b2f1ebe182"))
}
