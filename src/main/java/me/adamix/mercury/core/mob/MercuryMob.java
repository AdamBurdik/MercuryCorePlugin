package me.adamix.mercury.core.mob;

import lombok.Getter;
import me.adamix.mercury.core.attribute.MercuryAttribute;
import me.adamix.mercury.core.exception.MobNotSpawnedException;
import me.adamix.mercury.core.math.Pos;
import me.adamix.mercury.core.mob.attribute.MobAttributeContainer;
import me.adamix.mercury.core.mob.component.MercuryMobComponent;
import me.adamix.mercury.core.mob.component.MobAttributeComponent;
import org.bukkit.entity.Creature;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Mob;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@Getter
public class MercuryMob {
	private @Nullable Mob bukkitMob;
	private final @NotNull EntityType entityType;
	private final @NotNull String name;
	private final @NotNull MercuryMobComponent[] components;
	private long currentHealth;
	private Pos lastPosition;

	public MercuryMob(
			@NotNull EntityType entityType,
			@NotNull String name,
			@NotNull MercuryMobComponent[] components,
			long currentHealth
		) {
		this.entityType = entityType;
		this.name = name;
		this.components = components;
		this.currentHealth = currentHealth;
	}

	public MercuryMob(
			@NotNull EntityType entityType,
			@NotNull String name,
			@NotNull MobAttributeContainer attributes,
			long currentHealth
			) {
		this(entityType, name, new MercuryMobComponent[]{attributes.toComponent()}, currentHealth);
	}

	public boolean hasComponent(Class<? extends MercuryMobComponent> clazz) {
		return getComponent(clazz) != null;
	}

	public <T extends MercuryMobComponent> @Nullable T getComponent(Class<T> clazz) {
		for (@NotNull MercuryMobComponent itemComponent : components) {
			if (itemComponent.getClass().equals(clazz)) {
				if (clazz.isInstance(itemComponent)) {
					return clazz.cast(itemComponent);
				}
			}
		}
		return null;
	}

	/**
	 * Retrieves bukkit mob
	 * @return {@link Mob} bukkit mob instance
	 * @throws MobNotSpawnedException if mob has not been spawned yet.
	 */
	@ApiStatus.Internal
	public @NotNull Mob getBukkitMob() {
		if (bukkitMob == null) {
			throw new MobNotSpawnedException("Cannot retrieve bukkit mob! Mob has not been spawned!");
		}
		return bukkitMob;
	}

	public double getMaxHealth() {
		MobAttributeComponent component = getComponent(MobAttributeComponent.class);
		if (component == null) {
			return 0;
		}

		Double maxHealthValue = component.get(MercuryAttribute.MAX_HEALTH);
		return maxHealthValue != null ? maxHealthValue.floatValue() : 0f;
	}

	public void damage(long amount) {
		this.currentHealth -= amount;
		if (currentHealth < 0) {
			getBukkitMob().remove();
		}
	}
}
