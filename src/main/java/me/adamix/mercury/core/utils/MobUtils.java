package me.adamix.mercury.core.utils;

import org.bukkit.Location;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class MobUtils {
	public static Entity spawnBareboneEntity(@NotNull Location location, @NotNull EntityType type, @Nullable Consumer<Entity> function) {
		return location.getWorld().spawnEntity(location, type, CreatureSpawnEvent.SpawnReason.CUSTOM, entity -> {
			if (entity instanceof Ageable ageable) {
				ageable.setAdult();
//				ageable.setAI(false);
				ageable.getEquipment().clear();
				ageable.getActivePotionEffects().clear();
				ageable.setGlowing(false);
				ageable.setCustomNameVisible(false);
				ageable.setGravity(true);
				ageable.setRemoveWhenFarAway(true);
				ageable.setSilent(true);
			}

			if (function != null) {
				function.accept(entity);
			}
		});
	}
}
