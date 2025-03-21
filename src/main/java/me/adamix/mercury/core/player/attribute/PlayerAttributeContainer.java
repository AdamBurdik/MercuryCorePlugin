package me.adamix.mercury.core.player.attribute;

import me.adamix.mercury.core.attribute.MercuryAttribute;
import me.adamix.mercury.core.player.MercuryPlayer;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class PlayerAttributeContainer {
	private final EnumMap<MercuryAttribute, Double> attributeMap = new EnumMap<>(MercuryAttribute.class);

	public PlayerAttributeContainer() {
		setDefaults();
	}

	public void setDefaults() {
		// ToDo Set default attributes
//		set(MercuryAttribute.DAMAGE, (double) PlayerDefaults.getDamage());
//		set(MercuryAttribute.MAX_HEALTH, (double) PlayerDefaults.getMaxHealth());
//		set(MercuryAttribute.ATTACK_SPEED, PlayerDefaults.getAttackSpeed());
//		set(MercuryAttribute.MOVEMENT_SPEED, PlayerDefaults.getMovementSpeed());
	}

	public @NotNull PlayerAttributeContainer set(@NotNull MercuryAttribute attribute, @Nullable Double value) {
		if (value != null) {
			this.attributeMap.put(attribute, value);
		}
		return this;
	}

	public @NotNull PlayerAttributeContainer modify(@NotNull MercuryAttribute attribute, @NotNull Double value, @NotNull AttributeModifier.Operation operation) {
		double currentValue = get(attribute);

		double newValue = switch (operation) {
			case ADD_NUMBER -> currentValue += value;
			case ADD_SCALAR -> currentValue += currentValue * value;
			case MULTIPLY_SCALAR_1 -> currentValue *= (1 + value);
		};
		set(attribute, newValue);
		return this;
	}

	public boolean has(@NotNull MercuryAttribute attribute) {
		return attributeMap.containsKey(attribute);
	}

	public double get(@NotNull MercuryAttribute attribute) {
		Double value = attributeMap.get(attribute);
		if (value != null) {
			return value;
		}
		throw new RuntimeException("Player attribute " + attribute.name() + " is not available!");
	}

	public void apply(@NotNull MercuryPlayer player) {
		attributeMap.forEach((attribute, value) -> {
			Attribute defaultAttribute = attribute.getBukkitAttribute();
			if (defaultAttribute != null) {
				player.getBukkitAttribute(defaultAttribute).setBaseValue(value);
			}
		});
	}

	public Map<String, Object> serialize() {
		Map<String, Object> map = new HashMap<>();

		attributeMap.forEach((attribute, value) -> {
			map.put(attribute.name(), value);
		});

		return map;
	}

	public static PlayerAttributeContainer deserialize(Map<String, Object> map) {
		PlayerAttributeContainer attributes = new PlayerAttributeContainer();

		map.forEach((attributeString, valueObject) -> {
			MercuryAttribute attribute = MercuryAttribute.valueOf(attributeString);
			if (valueObject instanceof Double doubleValue) {
				attributes.set(attribute, doubleValue);
			}
		});

		return attributes;
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) return true;
		if (object == null || getClass() != object.getClass()) return false;
		PlayerAttributeContainer that = (PlayerAttributeContainer) object;
		return Objects.equals(attributeMap, that.attributeMap);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(attributeMap);
	}
}
