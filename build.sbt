// See README.md for license details.
val majorChiselVersion = "6"
val minorChiselVersion = "2.0"

val chiselVersion = majorChiselVersion + "." + minorChiselVersion

scalaVersion     := "2.13.12"
version          := chiselVersion
organization     := "Martoni"

lazy val root = (project in file("."))
  .settings(
    name := "chisNesPad",
    libraryDependencies ++= Seq(
      "org.chipsalliance" %% "chisel" % chiselVersion,
      "org.scalatest" %% "scalatest" % "3.2.16" % "test",
      "Martoni" %% "fpgamacro" % "6.2.1",
    ),
    scalacOptions ++= Seq(
      "-language:reflectiveCalls",
      "-deprecation",
      "-feature",
      "-Xcheckinit",
      "-Ymacro-annotations",
    ),
    addCompilerPlugin("org.chipsalliance" % "chisel-plugin" % chiselVersion cross CrossVersion.full),
  )
