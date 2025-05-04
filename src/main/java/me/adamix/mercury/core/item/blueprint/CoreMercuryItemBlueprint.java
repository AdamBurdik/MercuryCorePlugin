package me.adamix.mercury.core.item.blueprint;

import me.adamix.mercury.api.attribute.AttributeContainer;
import me.adamix.mercury.api.item.blueprint.MercuryItemBlueprint;
import me.adamix.mercury.api.item.component.MercuryItemComponent;
import me.adamix.mercury.core.item.CoreMercuryItem;
import net.kyori.adventure.key.Key;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public record CoreMercuryItemBlueprint(
		@NotNull Key blueprintKey,
		@NotNull Material material,
		@NotNull String name,
		@NotNull AttributeContainer attributeContainer,
		MercuryItemComponent[] components
) implements MercuryItemBlueprint {

	public @NotNull CoreMercuryItem build(UUID itemUniqueId) {
		return new CoreMercuryItem(
				itemUniqueId,
				this.blueprintKey,
				this.name,
				this.material,
				this.attributeContainer,
				this.components
		);
	}
}
