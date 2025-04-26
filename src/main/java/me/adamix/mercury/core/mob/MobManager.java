package me.adamix.mercury.core.mob;

import lombok.Getter;
import me.adamix.mercury.core.MercuryCore;
import me.adamix.mercury.core.MercuryCorePlugin;
import me.adamix.mercury.core.attribute.MercuryAttribute;
import me.adamix.mercury.core.event.mob.MobSpawnEvent;
import me.adamix.mercury.core.mob.blueprint.MercuryMobBlueprint;
import me.adamix.mercury.core.mob.blueprint.MobBlueprintParser;
import me.adamix.mercury.core.mob.component.MobAttributeComponent;
import me.adamix.mercury.core.mob.event.EventHandler;
import me.adamix.mercury.core.player.MercuryPlayer;
import me.adamix.mercury.core.utils.MobUtils;
import net.kyori.adventure.key.Key;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class MobManager {
	private final @NotNull Map<Key, MercuryMobBlueprint> blueprintRegistryMap;
	@Getter
	private final @NotNull Map<UUID, MercuryMob> mobMap = new HashMap<>();

	public MobManager() {
		this.blueprintRegistryMap = new HashMap<>();
	}

	/**
	 * Get all parsed entities from folder and registers them.
	 */
	public void registerAllEntityBlueprints(@NotNull String dataFolder) {
		List<MobBlueprintParser.MobBlueprintParseResult> resultList = MobBlueprintParser.parseAll(dataFolder + "/mobs");
		for (MobBlueprintParser.MobBlueprintParseResult result : resultList) {
			registerBlueprint(result.mobKey(), result.blueprint());
		}
	}

	/**
	 * Registers mob blueprint.
	 * @param blueprintkey key used to retrieve blueprint later.
	 * @param mobBlueprint blueprint to register.
	 */
	public void registerBlueprint(@NotNull Key blueprintkey, @NotNull MercuryMobBlueprint mobBlueprint) {
		MercuryCorePlugin.getCoreLogger().info("Registered entity blueprint: {}", blueprintkey);
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
		Mob bukkitMob = (Mob) MobUtils.spawnBareboneEntity(location, mob.getEntityType(), entity -> {
		// Apply attributes
		MobAttributeComponent attributeComponent = mob.getComponent(MobAttributeComponent.class);
			if (attributeComponent != null) {
				if (entity instanceof LivingEntity livingEntity) {
					attributeComponent.applyToEntity(livingEntity);
				}
			}
		});
		if (bukkitMob.isVisibleByDefault()) {
			for (MercuryPlayer player : MercuryCore.playerManager().getPlayers()) {
				mob.addViewer(player);
			}
		}

		mobMap.put(bukkitMob.getUniqueId(), mob);
		mob.setBukkitMob(bukkitMob);

		mob.updateName();

		EventHandler eventHandler = mob.getEventHandler();
		if (eventHandler != null) {
			eventHandler.onSpawn(new MobSpawnEvent(mob, location));
		}
	}

	public @NotNull Set<Key> getBlueprintRegistryKeySet() {
		return blueprintRegistryMap.keySet();
	}
}
