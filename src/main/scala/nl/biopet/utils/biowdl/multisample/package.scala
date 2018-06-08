package nl.biopet.utils.biowdl

package object multisample {
  case class Sample(name: String,
                    config: Map[String, Any] = Map(),
                    libraries: Map[String, Library] = Map()) {
    def toMap: Map[String, Any] =
      config + ("libraries" -> libraries.map {
        case (libName, lib) => libName -> lib.toMap
      })
  }
  case class Library(sample: String,
                     library: String,
                     config: Map[String, Any] = Map(),
                     readgroups: Map[String, Readgroup] = Map()) {
    def toMap: Map[String, Any] =
      config + ("readgroups" -> readgroups.map {
        case (rgName, rg) => rgName -> rg.toMap
      })
  }
  case class Readgroup(sample: String,
                       library: String,
                       readgroup: String,
                       config: Map[String, Any] = Map()) {
    def toMap: Map[String, Any] = config
  }
}
