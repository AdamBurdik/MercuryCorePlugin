package me.adamix.mercury.core.mob.blueprint;

import lombok.Data;
import me.adamix.mercury.core.mob.MercuryMob;
import me.adamix.mercury.core.mob.attribute.MobAttributeContainer;
import me.adamix.mercury.core.mob.component.MercuryMobComponent;
import me.adamix.mercury.core.mob.event.EventHandler;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Data
public class MercuryMobBlueprint {
	private final @NotNull EntityType entityType;
	private final @NotNull String name;
	private final @NotNull MercuryMobComponent[] components;
	private final @Nullable EventHandler eventHandler;

	public MercuryMobBlueprint(
			@NotNull EntityType entityType,
			@NotNull String name,
			@NotNull MercuryMobComponent[] components,
			@Nullable EventHandler eventHandler
	) {
		this.entityType = entityType;
		this.name = name;
		this.components = components;
		this.eventHandler = eventHandler;
	}

	public MercuryMobBlueprint(
			@NotNull EntityType entityType,
			@NotNull String name,
			@NotNull MobAttributeContainer attributes,
			@Nullable EventHandler eventHandler
	) {
		this.entityType = entityType;
		this.name = name;
		this.components = new MercuryMobComponent[]{attributes.toComponent()};
		this.eventHandler = eventHandler;
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

	public @NotNull MercuryMob build() {
		return new MercuryMob(
				entityType,
				name,
				components,
				eventHandler
		);
	}
}
