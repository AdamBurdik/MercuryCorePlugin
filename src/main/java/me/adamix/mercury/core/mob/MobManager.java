package me.adamix.mercury.core.mob;

import me.adamix.mercury.core.mob.component.MobAttributeComponent;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.Mob;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.jetbrains.annotations.NotNull;

public class MobManager {

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
