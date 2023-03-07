// See README.md for license details.

scalaVersion     := "2.13.8"
version          := "0.2.1"
organization     := "Martoni"

val majorChiselVersion = "3"
val minorChiselVersion = "5.4"
val chiselVersion = majorChiselVersion + "." + minorChiselVersion


lazy val root = (project in file("."))
  .settings(
    name := "chisNesPad",
    libraryDependencies ++= Seq(
      "edu.berkeley.cs" %% "chisel3" % chiselVersion,
      "edu.berkeley.cs" %% "chiseltest" % ("0." + minorChiselVersion) % "test",
      "Martoni" %% "fpgamacro" % "0.2.2",
    ),
    scalacOptions ++= Seq(
      "-Xsource:2.11",
      "-language:reflectiveCalls",
      "-deprecation",
      "-feature",
      "-Xcheckinit",
      "-P:chiselplugin:useBundlePlugin"
    ),
    addCompilerPlugin("edu.berkeley.cs" % "chisel3-plugin" % chiselVersion cross CrossVersion.full),
  )
