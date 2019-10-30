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

Before creating a command we first have to integrate brigadier into our environment.  
The most important class for us is [Brigadier](https://github.com/volixdev/bookshelf/blob/master/brigadier-core/src/main/java/net/volix/brigadier/Brigadier.java), as it handles our commands and the adapter we need to use.

### Adapting brigadier into your system

As every system can be different but we still want brigadier to do many things automatically, we have to setup some things before using it.  

First we decide that we want to use `Integer` as command source. For our example it represents the id of a specific user.  

Now we need to extend the class `CommandContext` as it represents the meta information of the command execution during the process.

```java
public class MyCommandContext extends CommandContext<Integer> {
	// we don't need to do anything else, but if we wanted to
	// we could add additional methods to this class.
	
	public MyCommandContext(Integer source, CommandInstance command, ParameterSet parameter) {
		super(source, command, parameter);
	}
	
}
```

But the command context alone is not enough, we need an *adapter*  to be able to tell brigadier how to behave.

```java
public class MyAdapter extends BrigadierAdapter<Integer> {
	/*
	  for this example we use "Integer" as command source, 
	  just think of it as an id for an user executing the command.
          but you can choose whatever type you want as source
	*/
	
	@Override
	public void handleRegister(final String label, final CommandInstance command) {
		/*
		  this method will be called if we register a command to brigadier
		  we can use this to register the command to _Bukkit_ or _BungeeCord_ for example
		*/
	}
	
	@Override
	public boolean checkPermission(final Integer commandSource, final CommandInstance command) {
		/*
		  in this method we can check if a command can be executed by the giving command source
		  in our example we just return true, every user can execute every command therefore
		*/
		return true;
	}
	
	@Override
	public void runAsync(final Runnable runnable) {
		/*
		  this method will be called if something needs to be executed asynchronously.
		  we started using the same approach for every project, 
		  but it turns out, that for some cases it is more efficient to 
		  do it differently (e.g. with _Bukkit's schedulers_)
		*/
	}
	
	@Override
	public Class<Integer> getCommandSourceClass() {
		/*
		  returns the class of the command source as we can't get 
		  the exact source class during runtime otherwise
		*/
		return Integer.class;
	}
	
	@Override
	public CommandContext<Integer> constructCommandContext(final Integer commandSource, final CommandInstance command, final ParameterSet parameter) {
		/*
		  returns the context we created earlier. 
		  This way brigadier can provide us the context automatically during execution
		*/
		return new MyCommantContext(commandSource, command, parameter);
	}
	
}
```

This class now needs to be passed down to the brigadier system. For this we use:
```java
Brigadier.getInstance().setAdapter(new MyAdapter());
```

Done!

### Creating a command

To be able to use and execute a command, you first have to define and create a command.  
A **command** in this system is represented by a method with specific signature and an annotation marking this method. It doesn't matter where the method is declared, there is no such restriction as an interface or an abstract class we need to extend.  
This could look like the following:

```java
// parameters should be self-explanatory, otherwise have a look at the source code
@Command(label = "myCommand", // label to access the command
         aliases = {"alias1", "alias2"},
         description = "That is our test command",
         permission = "test.permission")
public void exec(S commandSource, CommandContext context, ParameterSet parameter) {
     // logic of the command
}
```

### Defining subcommands

Sometimes it's not enough to have a pattern like `command <param1> <param2>`, but we want to have `command subcommand <params ...>`.  
To solve this problem, we can use the parent-child pattern in brigadier.  
Simply declare a subcommand like this:

```java
@Command(label = "mySubCommand",
         parent = "myCommand")
public void exec(S commandSource, CommandContext context, ParameterSet parameter) {
}
```

They will be automatically registered with the parent command, if the methods exist within the same specified range. For our example the range is defined as one single class. Otherwise we would have to use a different approach when registering commands.

### Parsing custom parameters

`ParameterSet` in brigadier has the ability to parse arguments to custom parameters. This way you can use `parameter.getInt(0)` instead of parsing it yourself. Or to be more abstract `parameter.get(0, Integer.class)`.  
To create a custom type, we just implement `ParameterType`:

```java
public class MyParameter implements ParameterType<MyObject> {
	@Override
	public MyObject parse(String string) {
		// put your parsing logic here
		if(string.isEmpty()) return null;
		return new MyObject(string);
	}
	
	@Override
	public Class<MyObject> getTypeClass() {
		return MyObject.class;
	}
}
```

Now we can register this type to brigadier with `Brigadier.getInstance().registerTypes(new MyParameter())`.  
To use the custom type, simply access `ParameterSet#get(index, MyParameter.class)`.

### Tab completion/suggestions

If the command source  is a real user, then it can be useful to give suggestions during the command input. That can be done in brigadier by first creating a _tab completion method_:

```java
@TabCompletor(command = "myCommand")
public List<String> onTabComplete(Integer commandSource, int index) {
	/* 
	  let's suppose for our example, that he wants to do 
	  arithmetic operations (usage: /arithm num1 oper num2)
	*/
	return index == 1 ? Arrays.asList("plus", "minus", "div", "times") : new ArrayList<>();
}
```

This method will be automatically registered with the command, if this method is in the same scope. We will have a look at the registering process later.

### Command usage syntax

`Command` includes a field `usage` which can be used to send help information to the user or to be able to decide how many parameters the user has to pass.  
An example of a common usage would be:
```java
@Command(label = "arithm", usage = "<num1> <operation> <num2> [-r]")
	...
```

In this example we have `arithm` as the command to execute an arithmetic operation.  
- `<..>` defines a **needed** parameter, so that the user has to pass down this argument.  
- `[..]` defines an **optional** parameter, so it can be ommited.  

The syntax of the usage can be defined via _regex_ with: `(([<\\[(])[a-zA-Z_0-9:|()-]+([>\\])])( )?)+`  
As you can see, only characters from `a-z`, `A-Z`, `_`, `0-9` and `-` are allowed

### Registering a command

### Executing commands

### Execution results and result handler
