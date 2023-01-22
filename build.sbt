val tapirVersion = "1.0.2"
val ZIOVersion = "2.0.1"
val ZIOConfigVersion = "3.0.1"

ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.2.2"

lazy val root = (project in file("."))
  .settings(
    name := "vaccination-api"
  )

libraryDependencies ++= Seq(
  "com.softwaremill.sttp.tapir" %% "tapir-zio-http-server" % tapirVersion,
  "com.softwaremill.sttp.tapir" %% "tapir-swagger-ui-bundle" % tapirVersion,
  "com.softwaremill.sttp.tapir" %% "tapir-json-zio" % tapirVersion,
  "io.d11" %% "zhttp" % "2.0.0-RC10",
  "dev.zio" %% "zio" % ZIOVersion,
  "dev.zio" %% "zio-config" % ZIOConfigVersion,
  "dev.zio" %% "zio-config-typesafe" % ZIOConfigVersion,
  "dev.zio" %% "zio-config-magnolia" % ZIOConfigVersion
)