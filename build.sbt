import sbt.ThisBuild
import sbt.url

scalaVersion := "2.13.16"

libraryDependencies ++= Seq(
  "com.eed3si9n"  %% "treehugger"  % "0.4.4",
  "org.typelevel" %% "cats-core"   % "2.13.0",
  "dev.valentiay" %% "phobos-core" % "0.26.0",
)

ThisBuild / updateOptions := updateOptions.value.withGigahorse(false)

ThisBuild / moduleName := "deimos-plugin"
ThisBuild / versionScheme := Some("early-semver")

ThisBuild / organization := "dev.valentiay"
ThisBuild / version := "0.1"
ThisBuild / publishMavenStyle := true
ThisBuild / publishTo := sonatypePublishToBundle.value

ThisBuild / scmInfo := Some(
  ScmInfo(
    url("https://github.com/valentiay/deimos"),
    "git@github.com:valentiay/deimos",
  ),
)

ThisBuild / developers := List(
  Developer(
    id = "valentiay",
    name = "Alexander Valentinov",
    email = "valentiay@yandex.ru",
    url = url("https://github.com/valentiay"),
  ),
)

ThisBuild / description := "Binaries for generating classes from XML Schemas for Phobos library"
ThisBuild / licenses := List("Apache 2" -> new URI("http://www.apache.org/licenses/LICENSE-2.0.txt").toURL)
ThisBuild / homepage := Some(url("https://github.com/valentiay/deimos"))

// Remove all additional repository other than Maven Central from POM
ThisBuild / pomIncludeRepository := { _ =>
  false
}

ThisBuild / credentials += Credentials(Path.userHome / ".sbt" / "sonatype_credential")

Compile / doc / sources := List.empty
