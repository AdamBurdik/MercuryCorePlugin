package me.adamix.mercury.core.item.blueprint;

import me.adamix.mercury.core.MercuryCorePlugin;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class ItemBlueprintManager {
	private final Map<Key, MercuryItemBlueprint> itemBlueprintMap = new HashMap<>();

	public void registerAllItemBlueprints(@NotNull String dataFolder) {
		List<MercuryItemBlueprint> resultList = ItemBlueprintParser.parseAll(dataFolder + "/mobs");
		for (MercuryItemBlueprint result : resultList) {
			registerBlueprint(result);
		}
	}

	public void unloadAllItems() {
		itemBlueprintMap.clear();
	}

	public void registerBlueprint(MercuryItemBlueprint item) {
		itemBlueprintMap.put(item.blueprintKey(), item);
		MercuryCorePlugin.getCoreLogger().info("Registered item blueprint: {}", item.blueprintKey());
	}

	public @NotNull Optional<MercuryItemBlueprint> get(Key blueprintKey) {
		return Optional.ofNullable(itemBlueprintMap.get(blueprintKey));
	}

	public boolean contains(Key blueprintKey) {
		return itemBlueprintMap.containsKey(blueprintKey);
	}

	public @NotNull Set<Key> getItemIdCollection() {
		return itemBlueprintMap.keySet();
	}

	public @NotNull Collection<MercuryItemBlueprint> getAllItems() {
		return this.itemBlueprintMap.values();
	}
}
