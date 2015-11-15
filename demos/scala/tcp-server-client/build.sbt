lazy val root = (project in file(".")).
    settings(
        organization := "com.github.pheymann",
        name := "tcp-server-client",
        version := "0.1.0",
        scalaVersion := "2.11.7",

        startYear := Some(2015),

        description :=
            """This is a demo project to test the Scala MockIt implementation for TCP
              |server and clients.""".stripMargin
    )

lazy val scalaBasicVersion = "2.11"

lazy val javaVersion = "1.8"

val scalaDependencies = Seq (
    "org.scalatest" % "scalatest_2.11" % "2.2.5" % Test,

    "org.scala-lang" % "scala-reflect" % "2.11.7",
    "org.scala-lang.modules" % "scala-xml_2.11" % "1.0.4",

    "com.github.pheymann" % s"mockit_$scalaBasicVersion" % "0.2.0-beta"
)

val javaDependencies = Seq (
    "ch.qos.logback" % "logback-classic" % "1.1.2" % Test
)

resolvers += "Local Maven Repository" at "file://"+Path.userHome.absolutePath+"/.m2/repository"

libraryDependencies ++= scalaDependencies
libraryDependencies ++= javaDependencies
