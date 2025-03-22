package me.adamix.mercury.core.attribute;

import me.adamix.mercury.core.MercuryCore;
import net.kyori.adventure.key.Key;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.AttributeModifier;
import org.jetbrains.annotations.NotNull;

public record MercuryAttributeModifier(
		@NotNull Key key,
		double value,
		@NotNull Operation operation
) {

	public @NotNull AttributeModifier toBukkit() {
		return new AttributeModifier(
				new NamespacedKey(key.namespace(), key.value()),
				value,
				operation.bukkitOperation
		);
	}

	public static enum Operation {
		ADD_VALUE(AttributeModifier.Operation.ADD_NUMBER),
		MULTIPLY_BASE(AttributeModifier.Operation.ADD_SCALAR),
		MULTIPLY_TOTAL(AttributeModifier.Operation.MULTIPLY_SCALAR_1);

		private final AttributeModifier.Operation bukkitOperation;

		Operation(AttributeModifier.Operation bukkitOperation) {
			this.bukkitOperation = bukkitOperation;
		}
	}
}
