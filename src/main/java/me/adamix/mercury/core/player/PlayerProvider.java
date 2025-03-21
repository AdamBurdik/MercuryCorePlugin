package me.adamix.mercury.core.player;

import me.adamix.mercury.core.player.inventory.MercuryPlayerInventory;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface PlayerProvider {
	@NotNull MercuryPlayer createPlayer(@NotNull Player bukkitPlayer, @NotNull String translationId);
}
