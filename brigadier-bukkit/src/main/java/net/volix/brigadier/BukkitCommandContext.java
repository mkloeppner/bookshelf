package net.volix.brigadier;

import net.volix.brigadier.command.CommandInstance;
import net.volix.brigadier.context.CommandContext;
import net.volix.brigadier.parameter.ParameterSet;
import org.bukkit.command.CommandSender;

/**
 * @author Tobias Büser
 */
public class BukkitCommandContext extends CommandContext<CommandSender> {

    public BukkitCommandContext(final CommandSender commandSource, final CommandInstance command, final ParameterSet parameter) {
        super(commandSource, command, parameter);
    }

}
