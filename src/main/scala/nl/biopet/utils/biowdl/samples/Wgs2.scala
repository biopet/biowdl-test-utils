package nl.biopet.utils.biowdl.samples

import nl.biopet.utils.biowdl.fixtureFile
import nl.biopet.utils.biowdl.multisample.{MultisamplePipeline, Sample}

trait Wgs2SingleEnd extends Wgs2SingleEndRg1 with Wgs2SingleEndRg2

trait Wgs2PairedEnd extends Wgs2PairedEndRg1 with Wgs2PairedEndRg2

trait Wgs2SingleEndRg1 extends MultisamplePipeline {
  override def samples: Map[String, Sample] =
    addReadgroup(super.samples,
      "wgs2",
      "lib1",
      "rg1",
      Map("R1" -> fixtureFile("samples", "wgs2", "wgs2-lib1_R1.fq.gz"),
        "R1_md5" -> "6fb02af910026041f9ea76cd28968732"))
}

trait Wgs2PairedEndRg1 extends Wgs2SingleEndRg1 {
  override def samples: Map[String, Sample] =
    addReadgroup(super.samples,
      "wgs2",
      "lib1",
      "rg1",
      Map("R2" -> fixtureFile("samples", "wgs2", "wgs2-lib1_R2.fq.gz"),
        "R2_md5" -> "537ffc52342314d839e7fdd91bbdccd0"))
}

trait Wgs2SingleEndRg2 extends MultisamplePipeline {
  override def samples: Map[String, Sample] =
    addReadgroup(super.samples,
      "wgs2",
      "lib1",
      "rg2",
      Map("R1" -> fixtureFile("samples", "wgs2", "wgs2-lib2_R1.fq.gz"),
        "R1_md5" -> "df64e84fdc9a2d7a9301f2aac0071aee"))
}

trait Wgs2PairedEndRg2 extends Wgs2SingleEndRg2 {
  override def samples: Map[String, Sample] =
    addReadgroup(super.samples,
      "wgs2",
      "lib1",
      "rg2",
      Map("R2" -> fixtureFile("samples", "wgs2", "wgs2-lib2_R2.fq.gz"),
        "R2_md5" -> "47a65ad648ac08e802c07669629054ea"))
}
