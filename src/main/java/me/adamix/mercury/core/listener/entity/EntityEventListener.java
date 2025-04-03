package me.adamix.mercury.core.listener.entity;

import me.adamix.mercury.core.MercuryCore;
import me.adamix.mercury.core.attribute.MercuryAttribute;
import me.adamix.mercury.core.event.mob.MobFireTickEvent;
import me.adamix.mercury.core.mob.MercuryMob;
import me.adamix.mercury.core.mob.MobManager;
import me.adamix.mercury.core.mob.component.MobAttributeComponent;
import me.adamix.mercury.core.mob.event.EventHandler;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
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

	@org.bukkit.event.EventHandler
	public void onEntityDamage(EntityDamageEvent event) {
		Entity bukkitEntity = event.getEntity();
		MercuryMob mob = MercuryCore.mobManager().getMob(bukkitEntity.getUniqueId());
		if (mob == null) {
			 return;
		}

		double givenDamage = event.getDamage();
		event.setDamage(0);

		MobAttributeComponent attributeComponent = mob.getComponent(MobAttributeComponent.class);
		if (attributeComponent == null) {
			return;
		}

		Double currentMobHealth = attributeComponent.get(MercuryAttribute.HEALTH);
		if (currentMobHealth == null) {
			return;
		}

		attributeComponent.set(MercuryAttribute.HEALTH, currentMobHealth - givenDamage);
		if (currentMobHealth - givenDamage <= 0) {
			if (bukkitEntity instanceof LivingEntity livingEntity) {
				livingEntity.setHealth(0);
			}
		}
	}
}
