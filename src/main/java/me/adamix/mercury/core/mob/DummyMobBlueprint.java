package me.adamix.mercury.core.mob;

import me.adamix.mercury.core.attribute.MercuryAttribute;
import me.adamix.mercury.core.event.mob.MobFireTickEvent;
import me.adamix.mercury.core.event.mob.MobSpawnEvent;
import me.adamix.mercury.core.mob.attribute.MobAttributeContainer;
import me.adamix.mercury.core.mob.blueprint.MercuryMobBlueprint;
import me.adamix.mercury.core.mob.event.EventHandler;
import org.bukkit.entity.EntityType;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public class DummyMobBlueprint extends MercuryMobBlueprint {
	public DummyMobBlueprint() {
		super(
				EntityType.ZOMBIE,
				"Dummy Mob! <red>Hello <player:name> <mob:type> <mob:health>/<mob:max_health>!",
				new MobAttributeContainer()
						.set(MercuryAttribute.DAMAGE, 100d)
						.set(MercuryAttribute.MAX_HEALTH, 2000d)
						.set(MercuryAttribute.HEALTH, 2000d),
				new DummyMobEventHandler()

		);
	}

	private static class DummyMobEventHandler implements EventHandler {
		@Override
		public void onSpawn(@NotNull MobSpawnEvent event) {
			event.getMob().getBukkitMob().setVelocity(new Vector(0, 0.8f, 0));
		}

		@Override
		public void onFireTick(@NotNull MobFireTickEvent event) {
			event.setCancelled(true);
			// Responsible for removing fire effect from mob.
			// ToDO Use EntityCombustEvent for daylight burning.
			event.getMob().getBukkitMob().setFireTicks(0);
		}
	}
}
