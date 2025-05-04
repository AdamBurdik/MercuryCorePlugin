package me.adamix.mercury.core.attribute;

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import me.adamix.mercury.api.attribute.MercuryAttributeInstance;
import me.adamix.mercury.api.attribute.MercuryAttributeModifier;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Map;

public class CoreMercuryAttributeInstance implements MercuryAttributeInstance {
	private final @NotNull Map<Key, MercuryAttributeModifier> modifierMap = new Object2ObjectArrayMap<>();
	private double value;

	public double value() {
		return this.value;
	}

	public CoreMercuryAttributeInstance(double value) {
		this.value = value;
	}

	public @NotNull CoreMercuryAttributeInstance setValue(double value) {
		this.value = value;
		return this;
	}

	public @NotNull MercuryAttributeInstance addModifier(@NotNull Key key, @NotNull MercuryAttributeModifier modifier) {
		modifierMap.put(key, modifier);
		return this;
	}

	public @Nullable MercuryAttributeModifier removeModifier(@NotNull Key key) {
		return modifierMap.remove(key);
	}

	public @Nullable MercuryAttributeModifier get(@NotNull Key key) {
		return modifierMap.get(key);
	}

	public @NotNull Collection<MercuryAttributeModifier> getModifiers() {
		return modifierMap.values();
	}

	public void clear() {
		modifierMap.clear();
	}

	public double calculateFinal() {
		double totalAdditive = 0.0;
		double totalMultiplyBase = 1.0;
		double totalMultiplyTotal = 1.0;

		for (MercuryAttributeModifier modifier : modifierMap.values()) {
			switch (modifier.operation()) {
				case CoreMercuryAttributeModifier.Operation.ADD_VALUE -> totalAdditive += modifier.value();
				case CoreMercuryAttributeModifier.Operation.MULTIPLY_BASE -> totalMultiplyBase += modifier.value();
				case CoreMercuryAttributeModifier.Operation.MULTIPLY_TOTAL -> totalMultiplyTotal *= (1.0 + modifier.value());
			}
		}

		return (value() * totalMultiplyBase + totalAdditive) * totalMultiplyTotal;
	}
}
