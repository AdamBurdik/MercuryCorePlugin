package me.adamix.mercury.core.item.blueprint;

import me.adamix.mercury.core.item.MercuryItem;
import me.adamix.mercury.core.item.component.MercuryItemComponent;
import net.kyori.adventure.key.Key;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * Represents item blueprint which containing all its generic data.
 * Typically used to define items loaded from config file
 */
public record MercuryItemBlueprint(@NotNull Key blueprintKey, @NotNull Material baseMaterial,
                                   @NotNull String name, MercuryItemComponent[] components) {
	public MercuryItemBlueprint(
			@NotNull Key blueprintKey,
			@NotNull Material baseMaterial,
			@NotNull String name,
			@NotNull MercuryItemComponent[] components
	) {
		this.blueprintKey = blueprintKey;
		this.baseMaterial = baseMaterial;
		this.name = name;
		this.components = components;
	}

	public @NotNull MercuryItem build(UUID itemUniqueId) {
		return new MercuryItem(
				itemUniqueId,
				this.blueprintKey,
				this.name,
				this.baseMaterial,
				this.components
		);
	}
}
