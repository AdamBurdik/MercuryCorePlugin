package me.adamix.mercury.core.attribute;

import lombok.Data;
import me.adamix.mercury.core.entity.MercuryEntity;
import org.apache.commons.lang.NotImplementedException;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.Map;

@Data
public class AttributeContainer {
	private final EnumMap<MercuryAttribute, MercuryAttributeInstance> attributeMap = new EnumMap<>(MercuryAttribute.class);

	public @NotNull AttributeContainer set(@NotNull MercuryAttribute attribute, @Nullable MercuryAttributeInstance instance) {
		this.attributeMap.put(attribute, instance);
		return this;
	}

	public boolean has(@NotNull MercuryAttribute attribute) {
		return attributeMap.containsKey(attribute);
	}

	public @Nullable MercuryAttributeInstance get(@NotNull MercuryAttribute attribute) {
		return attributeMap.get(attribute);
	}

	public void clear() {
		for (MercuryAttributeInstance instance : getAttributeMap().values()) {
			instance.clear();
		}
	}

	public void apply(@NotNull MercuryEntity entity) {
		LivingEntity livingEntity = entity.getLivingEntity();
		if (livingEntity == null) {
			return;
		}
		apply(livingEntity);
	}

	public void apply(@NotNull LivingEntity livingEntity) {
		attributeMap.forEach((attribute, instance) -> {
			Attribute defaultAttribute = attribute.getBukkitAttribute();
			if (defaultAttribute == null) {
				return;
			}

			AttributeInstance attributeInstance = livingEntity.getAttribute(defaultAttribute);
			if (attributeInstance == null) {
				livingEntity.registerAttribute(defaultAttribute);
				attributeInstance = livingEntity.getAttribute(defaultAttribute);
			}

			double finalValue = instance.calculateFinal();
			System.out.println(finalValue);
			attributeInstance.setBaseValue(finalValue);
		});
	}

	public Map<String, Object> serialize() {
		// ToDO Implement serialization and deserialization
		throw new NotImplementedException();
	}

	public static AttributeContainer deserialize(Map<String, Object> map) {
		// ToDO Implement serialization and deserialization
		throw new NotImplementedException();
	}
}
