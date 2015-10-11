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

See the Wiki for full documentation, examples, guide lines and other information.


# Project Structure

The structure of this project is follows:

 - mockit: sources and tests of the core library
 - mockit-daemon: stand alone application running the **MockIt Agent**
 - mockit-tool: command line tool to manage the daemons
 - demos: a collection of demo projects


# Binaries

## Maven (in review process: right noew not available)

To integrate MockIt into your Maven project just add it as dependency to
your POM:
```
<dependency>
    <groupId>org.mockit</groupId>
    <artifactId>mockit</artifactId>
    <version>x.y.z</version>
</dependency>
```

Replace the x.y.z with the correct version number you want to use.

## Small Example: Hello World

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


# Build

If you want to build your own jar files from the raw source code just 
download the code from GitHub:

`git clone git@https://github.com/MockIt`

and for the core library run:

```
$ cd MockIt/mockit
$ mvn clean package
```

for the daemon application:

```
$ cd MockIt/mockit-daemon
$ mvn clean package
```

and for the command line tool:

```
$ cd MockIt/mockit-tool
$ mvn clean package
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

For further information have a look into the extended guide.


# Licence

This project is licensed under the MIT Licence.


# External Projects

This projects are integrated into MockIt:

 - [reflections](https://github.com/ronmamo/reflections)
 - [JUnit](http://junit.org/)
