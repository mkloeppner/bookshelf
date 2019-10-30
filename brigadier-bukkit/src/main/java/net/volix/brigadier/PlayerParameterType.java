package net.volix.brigadier;

import java.util.UUID;
import net.volix.brigadier.parameter.ParameterType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * @author Tobias Büser
 */
public class PlayerParameterType implements ParameterType<Player> {

    @Override
    public Player parse(final String string) {
        try {
            final UUID uuid = UUID.fromString(string);
            return Bukkit.getPlayer(uuid);
        } catch (final Exception ex) {
            // string is not an unique id, ignore.
        }
        return Bukkit.getPlayerExact(string);
    }

    @Override
    public Class<Player> getTypeClass() {
        return Player.class;
    }
}
