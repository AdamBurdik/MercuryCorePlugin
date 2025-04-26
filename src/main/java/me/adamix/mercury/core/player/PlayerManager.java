package me.adamix.mercury.core.player;

import me.adamix.mercury.core.attribute.AttributeContainer;
import me.adamix.mercury.core.attribute.MercuryAttribute;
import me.adamix.mercury.core.attribute.MercuryAttributeInstance;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
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
		AttributeContainer attributeContainer = new AttributeContainer();
		attributeContainer.set(MercuryAttribute.MOVEMENT_SPEED, new MercuryAttributeInstance(0.1));
		attributeContainer.apply(bukkitPlayer);

		MercuryPlayer player = playerProvider.createPlayer(bukkitPlayer, "en", attributeContainer);
		playerMap.put(bukkitPlayer.getUniqueId(), player);
		return player;
	}

	public @Nullable MercuryPlayer getPlayer(@NotNull UUID uuid) {
		return playerMap.get(uuid);
	}

	public void removePlayer(@NotNull UUID uuid) {
		playerMap.remove(uuid);
	}

	public @NotNull Collection<MercuryPlayer> getPlayers() {
		return playerMap.values();
	}
}
