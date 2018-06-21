package nl.biopet.utils.biowdl.annotations

import java.io.File

import nl.biopet.utils.biowdl.references.Reference

trait Annotation extends Reference {
  def referenceGtf: Option[File] = None
  def referenceRefflat: Option[File] = None
}