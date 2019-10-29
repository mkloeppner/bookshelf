# brigadier

**brigadier** is a command assembling and dispatching system adapted from [Mojang/brigadier](https://github.com/Mojang/brigadier).  
Licensed under [GNU General Public License v2.0](https://github.com/volixdev/bookshelf/blob/master/LICENSE.md).  

Can be used in abstract environments, but works the best in **Minecraft** contexts as things like _permissions_ and different _command sources_ don't exist in every system.

## Installation

Currently we don't have a public location for this maven repository.
BUT assuming there is a place you get this repo from, include the following to your **pom.xml**:

```xml
<dependency>
    <groupId>net.volix.bookshelf</groupId>
    <artifactId>brigadier-core</artifactId>
    <version>(latest version)</version>
</dependency>
```

## Usage

### Creating a command

To be able to use and execute a command, you first have to define and create a command.  
A **command** in this system is represented by a method with specific signature and an annotation marking this method.  
This could look like the following:

```java
@Command(label = "myCommand")
public void exec(S commandSource, CommandContext context, ParameterSet parameter) {
     // logic of the command
}
```



- integration into your environment (adapter)
- creating a command
- command context and command parameters
- creating sub commands
- registering a command
- tab completion / suggestions
- custom parameters
- command usage parsing
- executing a command
- execution results and result handler
- examples
