ThisBuild / scalaVersion := "3.4.2"
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / organization := "com.example"
ThisBuild / organizationName := "example"

lazy val zioVersion = "2.1.6"
lazy val zioHttpVersion = "3.0.0-RC9"
lazy val zioJsonVersion = "0.6.2"

lazy val root = (project in file("."))
  .settings(
    name := "zio-http-swagger-example-scala3",
    libraryDependencies ++= Seq(
      "dev.zio" %% "zio" % zioVersion,
      "dev.zio" %% "zio-http" % zioHttpVersion,
      "dev.zio" %% "zio-json" % zioJsonVersion
    ) ++ testDependencies,
    scalacOptions ++= Seq("-Yretain-trees"),
    resolvers ++= Resolver.sonatypeOssRepos("snapshots"),
    testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework")
  )


lazy val testDependencies = Seq(
  "dev.zio" %% "zio-test" % zioVersion % Test,
  "dev.zio" %% "zio-test-sbt" % zioVersion % Test,
  "dev.zio" %% "zio-test-magnolia" % zioVersion % Test
)