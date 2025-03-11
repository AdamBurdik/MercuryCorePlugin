package me.adamix.mercury.core.mob;

import me.adamix.mercury.core.mob.blueprint.MercuryMobBlueprint;
import me.adamix.mercury.core.mob.component.MobAttributeComponent;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.Mob;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class MobManager {
	private final @NotNull Map<Key, MercuryMobBlueprint> blueprintRegistryMap;

	public MobManager() {
		this.blueprintRegistryMap = new HashMap<>();
	}

	/**
	 * Registers mob blueprint.
	 * @param blueprintkey key used to retrieve blueprint later.
	 * @param mobBlueprint blueprint to register.
	 */
	public void registerBlueprint(@NotNull Key blueprintkey, @NotNull MercuryMobBlueprint mobBlueprint) {
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

	/**
	 * Spawns a mob in world at specified location
	 * @param mob mob to spawn
	 * @param location location where mob should be spawned
	 */
	public void spawn(@NotNull MercuryMob mob, @NotNull Location location) {
		location.getWorld().spawnEntity(location, mob.getEntityType(), CreatureSpawnEvent.SpawnReason.CUSTOM, entity -> {
			Mob bukkitMob = mob.getBukkitMob();

			// Apply attributes
			MobAttributeComponent attributeComponent = mob.getComponent(MobAttributeComponent.class);
			if (attributeComponent != null) {
				attributeComponent.applyToMob(mob);
			}

			// Apply name
			// ToDO apply custom name using packets for specified player.
			bukkitMob.customName(Component.text(mob.getName()));
		});
	}
}
