package nl.biopet.utils.biowdl.annotations

import java.io.File

import nl.biopet.utils.biowdl.fixtureFile

trait TestAnnotation extends Annotation {
  override def referenceGtf: Option[File] =
    Some(fixtureFile("reference/reference.gtf"))
  override def referenceRefflat: Option[File] =
    Some(fixtureFile("reference/reference.refflat"))
}