See the documentation [README.md](docs/README.md) for more info on how to use 
the compiler and the C- specification.

# C- Compiler

This is a compiler for a simple language called C-. C- is a subset of C mostly 
defined in "Compiler Construction: Principles and Practice" by Kenneth C. 
Louden.

# Development

The C- compiler is written in [Java 8](https://en.wikipedia.org/wiki/Java_(programming_language)) 
and uses [Maven](https://maven.apache.org/) as the build tool. The Java JDK and 
Maven need to be installed before building (this only needs to be done once).


## How to install Java JDK and Maven

 1. Download and install the [Java JDK](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) for your platform.
 2. Set the `JAVA_HOME` environmental variable to the install directory for the Java SDK.
 3. [Download Apache Maven](https://maven.apache.org/download.cgi).
 4. [Install Maven](https://maven.apache.org/install.html).
 5. Add the Maven bin directory to the `PATH`.
 6. Execute `mvn -v` in the shell to verify that Maven and Java have been installed and configured correctly.

See [How do I set or change the PATH system variable?](https://www.java.com/en/download/help/path.xml) for more info on how to edit environmental variables.

## How to build the compiler and run tests

The following command builds, tests, and packages the compiler. The command 
should be run from the root directory, `cminus-compiler`.

```
mvn clean verify appassembler:assemble
```

After the command has been run the `target/output` directory will contain the 
documentation for the compiler as well as a `repo` folder that contains 
compiled '`jar`'s and a '`bin`' folder that contains scripts to run the 
'`jar`'s with java.

## How to setup Visual Studio Code

[Visual Studio Code](https://code.visualstudio.com/) is a cross-platform 
source code editor. This project is setup to use it as an editor.

 1. Download and install Visual Studio Code from the above link
 2. Install the [Java Language Support Extension](https://github.com/redhat-developer/vscode-java)
 3. Open the `cminus-compiler` folder in Visual Studio Code

 Tip: `CTRL+SHIFT+B` should build the project and run tests.