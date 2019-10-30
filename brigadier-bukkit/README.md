# brigadier-bukkit

This module adapts **brigadier** to bukkit.  
Either take this default plugin or build it yourself with the same classes to have it in your own system.  

## Features

We just created a custom parameter `PlayerParamType`, so that `ParameterSet#get(index, Player.class)` works.  
In this param type we parse either the name or the uuid to a player online on the same server.
