package me.adamix.mercury.core.mob;

import me.adamix.mercury.core.MercuryCore;
import me.adamix.mercury.core.MercuryCorePlugin;
import me.adamix.mercury.core.event.mob.MobSpawnEvent;
import me.adamix.mercury.core.mob.blueprint.MercuryMobBlueprint;
import me.adamix.mercury.core.mob.component.MobAttributeComponent;
import me.adamix.mercury.core.mob.event.EventHandler;
import me.adamix.mercury.core.player.MercuryPlayer;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MobManager {
	private final @NotNull Map<Key, MercuryMobBlueprint> blueprintRegistryMap;
	private final @NotNull Map<UUID, MercuryMob> mobMap = new HashMap<>();

	public MobManager() {
		this.blueprintRegistryMap = new HashMap<>();
	}

	/**
	 * Registers mob blueprint.
	 * @param blueprintkey key used to retrieve blueprint later.
	 * @param mobBlueprint blueprint to register.
	 */
	public void registerBlueprint(@NotNull Key blueprintkey, @NotNull MercuryMobBlueprint mobBlueprint) {
		MercuryCorePlugin.getCoreLogger().info("Entity '{}' has been registered", blueprintkey);
		blueprintRegistryMap.put(blueprintkey, mobBlueprint);
	}

	/**
	 * Retrieves mob blueprint.
	 * @param blueprintKey blueprint key.
	 * @return {@link MercuryMobBlueprint} instance or null if blueprint does not exist by specified key.
	 */
	public @Nullable MercuryMobBlueprint getBlueprint(@NotNull Key blueprintKey) {
		return blueprintRegistryMap.get(blueprintKey);
	}

	public @Nullable MercuryMob getMob(@NotNull UUID uuid) {
		return mobMap.get(uuid);
	}

	/**
	 * Spawns a mob in world at specified location
	 * @param mob mob to spawn
	 * @param location location where mob should be spawned
	 */
	public void spawn(@NotNull MercuryMob mob, @NotNull Location location) {
		 Mob bukkitMob = (Mob) location.getWorld().spawnEntity(location, mob.getEntityType(), CreatureSpawnEvent.SpawnReason.CUSTOM, entity -> {
			// Apply attributes
			MobAttributeComponent attributeComponent = mob.getComponent(MobAttributeComponent.class);
			if (attributeComponent != null) {
				attributeComponent.applyToMob(mob);
			}
		});
		mobMap.put(bukkitMob.getUniqueId(), mob);
		mob.setBukkitMob(bukkitMob);

		// Apply name
		for (Player bukkitPlayer : bukkitMob.getTrackedBy()) {
			MercuryPlayer player = MercuryCore.getPlayer(bukkitPlayer.getUniqueId());
			mob.updateName(player);
		}

		EventHandler eventHandler = mob.getEventHandler();
		if (eventHandler != null) {
			eventHandler.onSpawn(new MobSpawnEvent(mob, location));
		}
	}
}
