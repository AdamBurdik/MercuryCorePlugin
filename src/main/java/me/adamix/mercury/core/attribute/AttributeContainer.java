package me.adamix.mercury.core.attribute;

import lombok.Getter;
import me.adamix.mercury.core.MercuryCore;
import me.adamix.mercury.core.player.MercuryPlayer;
import me.adamix.mercury.core.player.attribute.PlayerAttributeContainer;
import net.kyori.adventure.key.Key;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Getter
public class AttributeContainer {
	private final EnumMap<MercuryAttribute, MercuryAttributeValue> attributeMap = new EnumMap<>(MercuryAttribute.class);

	public @NotNull AttributeContainer set(@NotNull MercuryAttribute attribute, @Nullable MercuryAttributeValue value) {
		this.attributeMap.put(attribute, value);
		return this;
	}

	public @NotNull AttributeContainer set(@NotNull MercuryAttribute attribute, double value, @NotNull AttributeModifier.Operation operation) {
		return this.set(attribute, new MercuryAttributeValue(value, operation));
	}

	public boolean has(@NotNull MercuryAttribute attribute) {
		return attributeMap.containsKey(attribute);
	}

	public @Nullable MercuryAttributeValue get(@NotNull MercuryAttribute attribute) {
		return attributeMap.get(attribute);
	}

	public void apply(@NotNull MercuryPlayer player) {
		attributeMap.forEach((attribute, value) -> {
			Attribute defaultAttribute = attribute.getDefaultAttribute();
			if (defaultAttribute != null) {
				player.getBukkitAttribute(defaultAttribute)
						.addModifier(new AttributeModifier(
								MercuryCore.namespacedKey(attribute.name()),
								value.value(),
								value.operation()

						));
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
		AttributeContainer that = (AttributeContainer) object;
		return Objects.equals(attributeMap, that.attributeMap);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(attributeMap);
	}
}
