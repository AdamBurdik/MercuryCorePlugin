package me.adamix.mercury.core.builders;

import me.adamix.mercury.api.builders.MercuryBuilderFactory;
import me.adamix.mercury.api.entity.EntityBuilder;
import me.adamix.mercury.api.entity.blueprint.EntityBlueprintBuilder;
import me.adamix.mercury.api.item.ItemBuilder;
import me.adamix.mercury.api.item.blueprint.ItemBlueprintBuilder;
import me.adamix.mercury.core.entity.CoreEntityBuilder;
import me.adamix.mercury.core.entity.blueprint.CoreEntityBlueprintBuilder;
import me.adamix.mercury.core.item.CoreItemBuilder;
import me.adamix.mercury.core.item.blueprint.CoreItemBlueprintBuilder;
import org.jetbrains.annotations.NotNull;

public class CoreBuilderFactory implements MercuryBuilderFactory {
	@Override
	public @NotNull ItemBlueprintBuilder itemBlueprint() {
		return new CoreItemBlueprintBuilder();
	}

	@Override
	public @NotNull ItemBuilder item() {
		return new CoreItemBuilder();
	}

	@Override
	public @NotNull EntityBuilder entity() {
		return new CoreEntityBuilder();
	}

	@Override
	public @NotNull EntityBlueprintBuilder entityBlueprint() {
		return new CoreEntityBlueprintBuilder();
	}
}
