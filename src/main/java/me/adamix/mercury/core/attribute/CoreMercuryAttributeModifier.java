package me.adamix.mercury.core.attribute;

import me.adamix.mercury.api.attribute.MercuryAttributeModifier;
import net.kyori.adventure.key.Key;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.AttributeModifier;
import org.jetbrains.annotations.NotNull;

public record CoreMercuryAttributeModifier(
		@NotNull Key key,
		double value,
		@NotNull Operation operation
) implements MercuryAttributeModifier {

	public @NotNull AttributeModifier toBukkit() {
		return new AttributeModifier(
				new NamespacedKey(key.namespace(), key.value()),
				value,
				operation.bukkitOperation()
		);
	}
}
