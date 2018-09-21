package nl.biopet.utils.biowdl.samples

import nl.biopet.utils.biowdl.fixtureFile
import nl.biopet.utils.biowdl.multisample.{MultisamplePipeline, Sample}

trait NA12878WGSSingleEnd extends NA12878WGSSingleEndL001 with NA12878WGSSingleEndL002
trait NA12878WGSPairedEnd extends NA12878WGSPairedEndL001 with NA12878WGSPairedEndL002

trait NA12878WGSSingleEndL001 extends MultisamplePipeline {
    override def samples: Map[String, Sample] = addReadgroup(
      super.samples,
      "NA12878",
      "U0a",
      "L001",
      Map(
        "reads" -> Map(
          "R1" -> fixtureFile("samples", "NA12878_wgs", "Sample_U0a", "U0a_CGATGT_L001_R1.fastq.gz"),
          "R1_md5" -> fixtureFile("samples", "NA12878_wgs", "Sample_U0a", "U0a_CGATGT_L001_R1.fastq.gz.md5")
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
        "R1" -> fixtureFile("samples", "NA12878_wgs", "Sample_U0a", "U0a_CGATGT_L002_R1.fastq.gz"),
        "R1_md5" -> fixtureFile("samples", "NA12878_wgs", "Sample_U0a", "U0a_CGATGT_L002_R1.fastq.gz.md5")
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
        "R2" -> fixtureFile("samples", "NA12878_wgs", "Sample_U0a", "U0a_CGATGT_L001_R2.fastq.gz"),
        "R2_md5" -> fixtureFile("samples", "NA12878_wgs", "Sample_U0a", "U0a_CGATGT_L001_R2.fastq.gz.md5")
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
        "R2" -> fixtureFile("samples", "NA12878_wgs", "Sample_U0a", "U0a_CGATGT_L002_R2.fastq.gz"),
        "R2_md5" -> fixtureFile("samples", "NA12878_wgs", "Sample_U0a", "U0a_CGATGT_L002_R2.fastq.gz.md5")
      )
    )
  )
}