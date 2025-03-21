package me.adamix.mercury.core.player;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerManager {
	private final @NotNull Map<UUID, MercuryPlayer> playerMap;
	private final PlayerProvider playerProvider = MercuryPlayer::new;

	public PlayerManager() {
		this.playerMap = new HashMap<>();
	}

	public @NotNull MercuryPlayer createPlayer(@NotNull Player bukkitPlayer) {
		// ToDO Get player data from database
		MercuryPlayer player = playerProvider.createPlayer(bukkitPlayer, "en");
		playerMap.put(bukkitPlayer.getUniqueId(), player);
		return player;
	}

	public @Nullable MercuryPlayer getPlayer(@NotNull UUID uuid) {
		return playerMap.get(uuid);
	}

	public void removePlayer(@NotNull UUID uuid) {
		playerMap.remove(uuid);
	}
}
