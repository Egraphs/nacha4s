import sbt._
import Keys._

object Nacha4SBuild extends Build {
  lazy val root = Project(id = "nacha4s",
                          base = file(".")
  ).settings(
    scalaVersion := "2.10.0",

    libraryDependencies += "joda-time" % "joda-time" % "2.2",
    libraryDependencies += "org.joda" % "joda-convert" % "1.3.1"
  )
}