lazy val root = (project in file(".")).
    settings(
        organization := "com.github.pheymann",
        name := "mockit-tool",
        version := "0.1.0",
        scalaVersion := "2.11.7",

        startYear := Some(2015),

        description :=
            """The MockIt Tool is a small command line application which can
              |start and stop MockIt Daemons on the local machine.""".stripMargin
    )

lazy val scalaBasicVersion = "2.11"

lazy val javaVersion = "1.8"

val scalaDependencies = Seq (
    "org.scala-lang" % "scala-reflect" % "2.11.7",
    "org.scala-lang.modules" % "scala-xml_2.11" % "1.0.4",

    "com.github.pheymann" % s"mockit_$scalaBasicVersion" % "0.1.0-beta"
)

val javaDependencies = Seq (
    "log4j" % "log4j" % "1.2.17"
)

resolvers += "Local Maven Repository" at "file://"+Path.userHome.absolutePath+"/.m2/repository"

libraryDependencies ++= javaDependencies
libraryDependencies ++= scalaDependencies

artifactName := {
    (sv: ScalaVersion, module: ModuleID, artifact: Artifact) =>
        artifact.name + "-" + module.revision + "." + artifact.extension
}

assemblyJarName in assembly := "mockit_tool.jar"