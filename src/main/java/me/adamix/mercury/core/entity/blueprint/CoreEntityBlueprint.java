package me.adamix.mercury.core.entity.blueprint;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import me.adamix.mercury.api.attribute.AttributeContainer;
import me.adamix.mercury.api.entity.MercuryEntity;
import me.adamix.mercury.api.entity.blueprint.MercuryEntityBlueprint;
import me.adamix.mercury.api.entity.component.MercuryEntityComponent;
import me.adamix.mercury.api.entity.type.MercuryEntityType;
import me.adamix.mercury.core.entity.CoreMercuryEntity;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public record CoreEntityBlueprint(
		@NotNull Key key,
		@NotNull MercuryEntityType type,
		long health,
		long maxHealth,
		long flags,
		@NotNull Set<MercuryEntityComponent> components,
		@Nullable AttributeContainer attributeContainer
) implements MercuryEntityBlueprint {
	@Override
	public @NotNull MercuryEntity build() {
		return new CoreMercuryEntity(
				key,
				type,
				health,
				maxHealth,
				flags,
				attributeContainer,
				components
		);
	}
}
