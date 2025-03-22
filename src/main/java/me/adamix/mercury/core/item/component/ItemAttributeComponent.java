package me.adamix.mercury.core.item.component;

import me.adamix.mercury.core.attribute.AttributeContainer;
import me.adamix.mercury.core.attribute.MercuryAttribute;
import me.adamix.mercury.core.attribute.MercuryAttributeInstance;
import me.adamix.mercury.core.attribute.MercuryAttributeModifier;
import org.apache.commons.lang.NotImplementedException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Objects;

public record ItemAttributeComponent(Map<MercuryAttribute, MercuryAttributeModifier> attributeMap) implements MercuryItemComponent {

	public @Nullable MercuryAttributeModifier get(MercuryAttribute attribute) {
		return attributeMap.get(attribute);
	}

	public void apply(@NotNull AttributeContainer attributeContainer) {
		// ToDo Apply custom attributes to player

		attributeMap.forEach((attribute, modifier) -> {
			MercuryAttributeInstance instance = attributeContainer.get(attribute);
			if (instance == null) {
				// ToDO Throw some error or indication that modifier has not been applied
				return;
			}
			instance.addModifier(modifier.key(), modifier);
		});
	}

	@Override
	public String name() {
		return "itemAttributeComponent";
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) return true;
		if (object == null || getClass() != object.getClass()) return false;
		ItemAttributeComponent that = (ItemAttributeComponent) object;
		return Objects.equals(attributeMap, that.attributeMap);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(attributeMap);
	}

	@Override
	public Map<String, Object> serialize() {
		// ToDO Implement serialization and deserialization
		throw new NotImplementedException();
	}

	public static @NotNull ItemAttributeComponent deserialize(Map<String, Object> map) {
		// ToDO Implement serialization and deserialization
		throw new NotImplementedException();
	}
}
