package me.adamix.mercury.core.entity.blueprint;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import me.adamix.mercury.api.attribute.AttributeContainer;
import me.adamix.mercury.api.entity.MercuryEntity;
import me.adamix.mercury.api.entity.blueprint.EntityBlueprintBuilder;
import me.adamix.mercury.api.entity.blueprint.MercuryEntityBlueprint;
import me.adamix.mercury.api.entity.component.MercuryEntityComponent;
import me.adamix.mercury.api.entity.type.MercuryEntityType;
import me.adamix.mercury.core.entity.CoreMercuryEntity;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

public class CoreEntityBlueprintBuilder implements EntityBlueprintBuilder {
	private @Nullable Key key;
	private @Nullable MercuryEntityType type;
	private long health = Long.MAX_VALUE;
	private long maxHealth = Long.MAX_VALUE;
	private long flags = 0;
	private @Nullable AttributeContainer attributeContainer;
	private final @NotNull Set<MercuryEntityComponent> componentSet = new ObjectOpenHashSet<>();

	@Override
	public @NotNull EntityBlueprintBuilder key(@NotNull Key key) {
		this.key = key;
		return this;
	}

	@Override
	public @NotNull EntityBlueprintBuilder type(@NotNull MercuryEntityType type) {
		this.type = type;
		return this;
	}

	@Override
	public @NotNull EntityBlueprintBuilder components(@NotNull MercuryEntityComponent... components) {
		componentSet.addAll(List.of(components));
		return this;
	}

	@Override
	public @NotNull EntityBlueprintBuilder health(long health) {
		this.health = health;
		return this;
	}

	@Override
	public @NotNull EntityBlueprintBuilder maxHealth(long maxHealth) {
		this.maxHealth = maxHealth;
		return this;
	}

	@Override
	public @NotNull EntityBlueprintBuilder attributes(@NotNull AttributeContainer attributeContainer) {
		this.attributeContainer = attributeContainer;
		return this;
	}

	@Override
	public @NotNull EntityBlueprintBuilder flags(long flags) {
		this.flags = flags;
		return this;
	}

	@Override
	public @NotNull MercuryEntityBlueprint build() {
		if (key == null) {
			throw new IllegalStateException("Missing required field: key");
		}
		if (type == null) {
			throw new IllegalStateException("Missing required field: type");
		}

		return new CoreEntityBlueprint(
				key,
				type,
				health,
				maxHealth,
				flags,
				componentSet,
				attributeContainer
		);
	}
}
