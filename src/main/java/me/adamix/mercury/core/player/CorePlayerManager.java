package me.adamix.mercury.core.player;

import me.adamix.mercury.api.attribute.AttributeContainer;
import me.adamix.mercury.api.attribute.MercuryAttribute;
import me.adamix.mercury.api.player.MercuryPlayer;
import me.adamix.mercury.api.player.PlayerManager;
import me.adamix.mercury.core.attribute.CoreAttributeContainer;
import me.adamix.mercury.core.attribute.CoreMercuryAttributeInstance;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CorePlayerManager implements PlayerManager {
	private final @NotNull Map<UUID, MercuryPlayer> playerMap;

	public CorePlayerManager() {
		this.playerMap = new HashMap<>();
	}

	@Override
	public @NotNull MercuryPlayer create(@NotNull Player bukkitPlayer) {
		// ToDO Get player data from database
		AttributeContainer attributeContainer = new CoreAttributeContainer();
		attributeContainer.set(MercuryAttribute.MOVEMENT_SPEED, new CoreMercuryAttributeInstance(0.1));
		attributeContainer.apply(bukkitPlayer);

		MercuryPlayer player = new CoreMercuryPlayer(
				bukkitPlayer,
				"en",
				attributeContainer
		);
		playerMap.put(bukkitPlayer.getUniqueId(), player);
		return player;
	}

	@Override
	public @Nullable MercuryPlayer get(@NotNull UUID uuid) {
		return playerMap.get(uuid);
	}

	@Override
	public void remove(@NotNull UUID uuid) {
		playerMap.remove(uuid);
	}

	@Override
	public @NotNull Collection<MercuryPlayer> getOnlinePlayers() {
		return playerMap.values();
	}
}
