package me.adamix.mercury.core.entity;

import me.adamix.mercury.core.attribute.AttributeContainer;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

public interface MercuryEntity {
	@Nullable LivingEntity getLivingEntity();
}
