package me.adamix.mercury.core.item;


import me.adamix.mercury.core.MercuryCore;
import me.adamix.mercury.core.item.blueprint.MercuryItemBlueprint;
import net.kyori.adventure.key.Key;

import java.util.Optional;
import java.util.UUID;


public class ItemManager {
	public Optional<MercuryItem> buildItem(Key blueprintID) {
		Optional<MercuryItemBlueprint> optionalBlueprint = MercuryCore.itemBlueprintManager().get(blueprintID);
		if (optionalBlueprint.isEmpty()) {
			return Optional.empty();
		}

		MercuryItemBlueprint blueprint = optionalBlueprint.get();
		return Optional.of(blueprint.build(UUID.randomUUID()));
	}
}
