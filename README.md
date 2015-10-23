# MockIt: Mock-Ups in a Distributed Environment

MockIt is an open source mock up framework for Scala and Java originally written by Paul Heymann. 
It was released in October 2015.

A [mock-up](https://en.wikipedia.org/wiki/Mockup#Software_engineering) simulates the 
behaviour of a certain interface or module without actually implementing it. It is 
used for unit tests, during the development of complex software projects or prototyping.

MockIt itself is a framework to mock services or clients in a distributed environment
and to simulate some of the familiar problems accompanied with distribution in a 
network like:

 - message transmission delay
 - message loss
 - message duplication
 - ...

Therefore it runs the mock instances as threads on the local machine or as stand alone
services on remote machines.
By now the following protocols are supported:

 - tcp
 - udp
 - http

MockIt provides an easy to use interface to create, run and manage the mocks
from within your code.


# Full Documentation

See the [Wiki](https://github.com/pheymann/MockIt/wiki) for full documentation, examples, guide lines and other information.


# Contact

There is a [Google Group](https://groups.google.com/forum/#!forum/comgithubpheymannmockit/new) where you can
ask questions.


# Project Structure

The structure of this project is follows:

 - mockit: sources and tests of the core library
 - mockit-java-api: sources and tests of the Java interface
 - mockit-daemon: stand alone application running the **MockIt Agent**
 - mockit-tool: command line tool to manage the daemons
 - demos: a collection of demo projects
 - images: images for the [Wiki](https://github.com/pheymann/MockIt/wiki)


# Binaries

## Releases

If you want to have the Jar-files directly just download them from the
the release [page](https://github.com/pheymann/MockIt/releases). There your get
the core library, the command line tool and the daemon application.

## SBT

To integrate MockIt into your SBT project just add it as dependency to
your build file:

```
lazy val scalaVersion = "2.11"

"com.github.pheymann" % s"mockit_$scalaVersion" % "x.y.z"
```

Replace the x.y.z with the correct version number you want to use (*current: 0.1.0-beta*).

## Maven

To integrate MockIt into your Maven project just add it as dependency to
your POM:

```
<dependency>
    <groupId>com.github.pheymann</groupId>
    <artifactId>mockit</artifactId>
    <version>x.y.z</version>
</dependency>
```

If you programming in Java you will need the Java API too:

```
<dependency>
    <groupId>com.github.pheymann</groupId>
    <artifactId>mockitjavaapi</artifactId>
    <version>x.y.z</version>
</dependency>
```
Replace the x.y.z with the correct version number you want to use (*current: 0.1.0-beta*).

## Small Example: Hello World

If you want to run one or several mock-ups on different machines you have to
start **MockIt Daemons** with the **MockIt Tool** on this machines 
([description](https://github.com/pheymann/MockIt/wiki/How-To-Use#run-a-daemon)). The number of
daemons has to be at least the number of mock-ups you want to run.

Otherwise if you use the library und create your own stand alone mock-up 
([example](https://github.com/pheymann/MockIt/wiki/How-To-Use#http-protocol)) daemons are not necessary at all.

This example shows how to create a small HTTP server:

```Scala
class HttpUnit extends HttpServerMockUnit {

    override def init: Unit = {
        // add a request-response pair
        add(
            HttpRequest(Get, "/webapp/test"),
            HttpResponse(OK)                        +
                ("this is a key", "and the value")  ++
                ("text/plain", "hello world".getBytes("UTF-8"))
        )
    }

}
```

This is the Mock Unit which implements the servers behaviour. In this
example the server just sends the response **hello world** when a call 
on `localhost:8080/webapp/test` appears.

To run the server just call:

```Scala
def main(args: Array[String]): Unit = {
    val unit = classOf[HttpUnit]

    // configure the server
    val container = new MockUnitContainer(
        unit.getCanonicalName,
        unit,
        new ServerConfiguration(
            8080,
            1,
            ConnectionType.http
        )
    )

    val shutdown = new ShutdownLatch
    val latch = new ShutdownLatch

    // run the mock in the local process
    MockIt.mockLocal(container :: Nil, shutdown, latch).call
}
```

The server will run in the current process as a thread and stops when
the parent process stops.

For more examples take a look [here](https://github.com/pheymann/MockIt/wiki/How-To-Use)


# Build

If you want to build your own jar files from the raw source code just 
download the code from GitHub:

`git clone git@https://github.com/MockIt`

and for the core library run:

```
$ cd MockIt/mockit
$ sbt package
```

for the Java API:

```
$ cd MockIt/mockit-java-api
$ mvn clean package
```

for the daemon application:

```
$ cd MockIt/mockit-daemon
$ sbt assembly
```

and for the command line tool:

```
$ cd MockIt/mockit-tool
$ sbt assembly
```

# Development Guide

This project is written in Scala and maintains interfaces for Java. If you
want to contribute to the project you should keep the following rules and 
principles in mind:

 - [DRY](https://en.wikipedia.org/wiki/Don%27t_repeat_yourself) (don't repeat your self),
 - refactore the code if it is necessary,
 - write your code like a book, easy to read and understandable and use
   comments when necessary,
 - cover every new functionality directly or indirectly with in a unit test,
 - keep all available unit tests running,
 - do not push faulty code into the repository,
 - no IDE files in the repository.

For further information have a look into the [extended guide](https://github.com/pheymann/MockIt/wiki/How-To-Contribute).


# Licence

This project is licensed under the MIT Licence.


# External Projects

This projects are integrated into MockIt:

 - [reflections](https://github.com/ronmamo/reflections)
