lazy val root = (project in file(".")).
    settings(
        organization := "com.github.pheymann",
        name := "mockit",
        version := "0.1.0",
        scalaVersion := "2.11.7",

        startYear := Some(2015),

        description :=
            """MockIt is a mock-up framework to simulate the behaviour and protocols of
              |remote clients and services without actually implementing them and keep the
              |characteristics of a distributed environment.""".stripMargin
    )

lazy val scalaBasicVersion = "2.11"

lazy val javaVersion = "1.8"

lazy val scalaTest = "scalatest_" + scalaBasicVersion

val scalaDependencies = Seq (
    "org.scalatest" % "scalatest_2.11" % "2.2.5" % Test,

    "org.scala-lang" % "scala-reflect" % "2.11.7",
    "org.scala-lang.modules" % "scala-xml_2.11" % "1.0.4"
)

val javaDependencies = Seq (
    "log4j" % "log4j" % "1.2.17",

    "org.apache.commons" % "commons-lang3" % "3.4",
    "commons-io" % "commons-io" % "2.4",

    "org.reflections" % "reflections" % "0.9.10"
)

libraryDependencies ++= javaDependencies
libraryDependencies ++= scalaDependencies

javacOptions ++= Seq("-source", javaVersion)

compileOrder := CompileOrder.JavaThenScala

publishMavenStyle := true
publishArtifact in Test := false

artifactName := {
    (sv: ScalaVersion, module: ModuleID, artifact: Artifact) =>
        artifact.name + "-" + module.revision + "." + artifact.extension
}

isSnapshot := true

sonatypeProfileName := "pheymann"

pomExtra in Global := {
        <licenses>
            <license>
                <name>MIT License</name>
                <url>https://opensource.org/licenses/MIT</url>
                <distribution>repo</distribution>
            </license>
        </licenses>

        <scm>
            <url>http://github.com/pheymann/MockIt</url>
            <developerConnection>scm:git:http://github.com/pheymann/MockIt</developerConnection>
            <connection>scm:git:http://github.com/pheymann/MockIt</connection>
        </scm>

        <developers>
            <developer>
                <id>pheymann</id>
                <name>Paul Heymann</name>
                <email>ph.privatac@gmail.com</email>
            </developer>
        </developers>
}