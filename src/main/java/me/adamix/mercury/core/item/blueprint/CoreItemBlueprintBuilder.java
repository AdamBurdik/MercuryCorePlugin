package me.adamix.mercury.core.item.blueprint;

import me.adamix.mercury.api.attribute.AttributeContainer;
import me.adamix.mercury.api.item.blueprint.MercuryItemBlueprint;
import me.adamix.mercury.api.item.blueprint.ItemBlueprintBuilder;
import me.adamix.mercury.api.item.component.MercuryItemComponent;
import net.kyori.adventure.key.Key;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;


public class CoreItemBlueprintBuilder implements ItemBlueprintBuilder {
	private Key key;
	private String name;
	private Material material;
	private AttributeContainer attributeContainer;
	private MercuryItemComponent[] components;

	@Override
	public @NotNull ItemBlueprintBuilder key(@NotNull Key key) {
		this.key = key;
		return this;
	}

	@Override
	public @NotNull ItemBlueprintBuilder name(@NotNull String name) {
		this.name = name;
		return this;
	}

	@Override
	public @NotNull ItemBlueprintBuilder material(@NotNull Material material) {
		this.material = material;
		return this;
	}

	@Override
	public @NotNull ItemBlueprintBuilder attributes(@NotNull AttributeContainer attributeContainer) {
		this.attributeContainer = attributeContainer;
		return this;
	}

	@Override
	public @NotNull ItemBlueprintBuilder components(@NotNull MercuryItemComponent... components) {
		this.components = components;
		return this;
	}

	@Override
	public @NotNull MercuryItemBlueprint build() {
		if (this.key == null) {
			throw new IllegalStateException("Missing required field: key");
		}
		if (this.components == null) {
			this.components = new MercuryItemComponent[0];
		}

		return new CoreMercuryItemBlueprint(
				this.key,
				this.material,
				this.name,
				this.attributeContainer,
				this.components
		);
	}
}
