package net.volix.brigadier;

import net.md_5.bungee.api.CommandSender;
import net.volix.brigadier.command.CommandInstance;
import net.volix.brigadier.context.CommandContext;
import net.volix.brigadier.parameter.ParameterSet;

/**
 * @author Tobias Büser
 */
public class BungeeCommandContext extends CommandContext<CommandSender> {

    public BungeeCommandContext(CommandSender commandSource, CommandInstance command, ParameterSet parameter) {
        super(commandSource, command, parameter);
    }

}
