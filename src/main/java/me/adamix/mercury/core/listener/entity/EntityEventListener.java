package me.adamix.mercury.core.listener.entity;

import me.adamix.mercury.core.MercuryCore;
import me.adamix.mercury.core.event.mob.MobFireTickEvent;
import me.adamix.mercury.core.mob.MercuryMob;
import me.adamix.mercury.core.mob.MobManager;
import me.adamix.mercury.core.mob.event.EventHandler;
import org.bukkit.entity.Entity;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class EntityEventListener implements Listener {
	@org.bukkit.event.EventHandler
	public void onFireTick(EntityDamageEvent event) {
		Entity entity = event.getEntity();
		MobManager mobManager = MercuryCore.mobManager();
		MercuryMob mob = mobManager.getMob(entity.getUniqueId());
		if (mob == null) {
			return;
		}

		EventHandler eventHandler = mob.getEventHandler();
		if (eventHandler == null) {
			return;
		}

		boolean cancelled = false;

		if (event.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK) {
			MobFireTickEvent mercuryEvent = new MobFireTickEvent(mob);
			eventHandler.onFireTick(mercuryEvent);
			cancelled = mercuryEvent.isCancelled();
		}

		if (cancelled) {
			event.setCancelled(true);
		}
	}
}
