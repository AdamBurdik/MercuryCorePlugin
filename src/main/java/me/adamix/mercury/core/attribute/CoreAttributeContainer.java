package me.adamix.mercury.core.attribute;

import lombok.Data;
import me.adamix.mercury.api.attribute.AttributeContainer;
import me.adamix.mercury.api.attribute.MercuryAttribute;
import me.adamix.mercury.api.attribute.MercuryAttributeInstance;
import me.adamix.mercury.api.attribute.MercuryAttributeModifier;
import me.adamix.mercury.api.entity.MercuryEntity;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;

@SuppressWarnings("UnstableApiUsage")
@Data
public class CoreAttributeContainer implements AttributeContainer {
	private final EnumMap<MercuryAttribute, MercuryAttributeInstance> attributeMap = new EnumMap<>(MercuryAttribute.class);

	public @NotNull CoreAttributeContainer set(@NotNull MercuryAttribute attribute, @Nullable CoreMercuryAttributeInstance instance) {
		this.attributeMap.put(attribute, instance);
		return this;
	}

	public @NotNull CoreAttributeContainer set(@NotNull MercuryAttribute attribute, @Nullable Double value) {
		if (value != null) {
			set(attribute, new CoreMercuryAttributeInstance(value));
		}
		return this;
	}

	@Override
	public @NotNull AttributeContainer set(@NotNull MercuryAttribute attribute, me.adamix.mercury.api.attribute.MercuryAttributeInstance instance) {
		attributeMap.put(attribute, instance);
		return this;
	}

	@Override
	public boolean has(@NotNull MercuryAttribute attribute) {
		return attributeMap.containsKey(attribute);
	}

	@Override
	public @Nullable MercuryAttributeInstance get(@NotNull MercuryAttribute attribute) {
		return attributeMap.get(attribute);
	}

	@Override
	public void clear() {
		for (MercuryAttributeInstance instance : getAttributeMap().values()) {
			instance.clear();
		}
	}

	@Override
	public void merge(@NotNull AttributeContainer attributeContainer) {
		attributeMap.forEach((attribute, instance) -> {
			MercuryAttributeInstance otherInstance = attributeContainer.get(attribute);
			for (MercuryAttributeModifier modifier : instance.getModifiers()) {
				otherInstance.addModifier(modifier.key(), modifier);
			}
		});
	}

	@Override
	public void apply(@NotNull MercuryEntity entity) {
		Entity bukitEntity = entity.bukkitEntity();
		if (bukitEntity instanceof LivingEntity livingEntity) {
			apply(livingEntity);
		}
	}

	@Override
	public void apply(@NotNull LivingEntity livingEntity) {
		attributeMap.forEach((attribute, instance) -> {
			Attribute defaultAttribute = attribute.bukkitAttribute();
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
}
