organization := "com.github.biopet"
organizationName := "Biopet"
name := "biowdl-test-utils"

biopetUrlName := "biowdl-test-utils"

startYear := Some(2018)

biopetIsTool := false

developers ++= List(
  Developer(id = "ffinfo",
            name = "Peter van 't Hof",
            email = "pjrvanthof@gmail.com",
            url = url("https://github.com/ffinfo")),
  Developer(id = "rhpvorderman",
            name = "Ruben Vorderman",
            email = "r.h.p.vorderman@lumc.nl",
            url = url("https://github.com/rhpvorderman"))
)

crossScalaVersions := Seq("2.11.12", "2.12.5")

scalaVersion := "2.11.12"

libraryDependencies += "com.github.biopet" %% "common-utils" % "0.5"
libraryDependencies += "com.github.biopet" %% "test-utils" % "0.4"

libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.5"
libraryDependencies += "org.mockito" % "mockito-all" % "1.10.19"
libraryDependencies += "org.testng" % "testng" % "6.14.3"
