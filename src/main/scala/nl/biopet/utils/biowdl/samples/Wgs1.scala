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

trait Wgs1PairedEnd extends Wgs1SingleEnd {
  override def samples: Map[String, Sample] =
    addReadgroup(super.samples,
      "wgs1",
      "lib1",
      "rg1",
      Map("R2" -> fixtureFile("samples", "wgs1", "R2.fq.gz"),
        "R2_md5" -> "986acc7bda0bf2ef55c52431f54fe3a9"))
}

