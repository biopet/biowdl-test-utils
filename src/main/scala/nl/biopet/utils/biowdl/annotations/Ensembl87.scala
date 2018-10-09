package nl.biopet.utils.biowdl.annotations

import java.io.File

import nl.biopet.utils.biowdl.fixtureFile
import nl.biopet.utils.biowdl.references.GRCh38_no_alt_analysis_set

trait Ensembl87 extends Annotation with GRCh38_no_alt_analysis_set {
  def referenceGtf: Option[File] = Some(fixtureFile("references/H.sapiens/GRCh38_no_alt_analysis_set/annotation/features/ensembl_87/ensembl.87.gtf"))
  def referenceRefflat: Option[File] =
    Some(fixtureFile("references/H.sapiens/GRCh38_no_alt_analysis_set/annotation/features/ensembl_87/ensembl.87.refflat"))
}
