package me.adamix.mercury.core.attribute;

import lombok.Getter;
import net.kyori.adventure.translation.Translatable;
import org.bukkit.attribute.Attribute;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Getter
public enum MercuryAttribute implements Translatable {
	DAMAGE,
	ATTACK_SPEED(Attribute.ATTACK_SPEED),
	MOVEMENT_SPEED(Attribute.MOVEMENT_SPEED),
	MAX_HEALTH,
	HEALTH;

	@Nullable
	private final Attribute bukkitAttribute;

	MercuryAttribute() {
		this(null);
	}

	MercuryAttribute(@Nullable Attribute bukkitAttribute) {
		this.bukkitAttribute = bukkitAttribute;
	}

	@Override
	public @NotNull String translationKey() {
		return "attribute." + this.name().toLowerCase();
	}
}
