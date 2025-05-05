package me.adamix.mercury.core.entity;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import me.adamix.mercury.api.attribute.AttributeContainer;
import me.adamix.mercury.api.entity.EntityBuilder;
import me.adamix.mercury.api.entity.MercuryEntity;
import me.adamix.mercury.api.entity.component.MercuryEntityComponent;
import me.adamix.mercury.api.entity.type.MercuryEntityType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

public class CoreEntityBuilder implements EntityBuilder {
	private @Nullable MercuryEntityType type;
	private long health = Long.MAX_VALUE;
	private long maxHealth = Long.MAX_VALUE;
	private long flags = 0;
	private @Nullable AttributeContainer attributeContainer;
	private final @NotNull Set<MercuryEntityComponent> componentSet = new ObjectOpenHashSet<>();

	@Override
	public @NotNull EntityBuilder type(@NotNull MercuryEntityType type) {
		this.type = type;
		return this;
	}

	@Override
	public @NotNull EntityBuilder components(@NotNull MercuryEntityComponent... components) {
		componentSet.addAll(List.of(components));
		return this;
	}

	@Override
	public @NotNull EntityBuilder health(long health) {
		this.health = health;
		return this;
	}

	@Override
	public @NotNull EntityBuilder maxHealth(long maxHealth) {
		this.maxHealth = maxHealth;
		return this;
	}

	@Override
	public @NotNull EntityBuilder attributes(@NotNull AttributeContainer attributeContainer) {
		this.attributeContainer = attributeContainer;
		return this;
	}

	@Override
	public @NotNull EntityBuilder flags(long flags) {
		this.flags = flags;
		return this;
	}

	@Override
	public @NotNull MercuryEntity build() {
		if (type == null) {
			throw new IllegalStateException("Missing required field: type");
		}

		return new CoreMercuryEntity(
				null,
				type,
				health,
				maxHealth,
				flags,
				attributeContainer,
				componentSet
		);
	}
}
