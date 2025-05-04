package me.adamix.mercury.core.item.blueprint;

import me.adamix.mercury.api.item.blueprint.ItemBlueprintManager;
import me.adamix.mercury.api.item.blueprint.MercuryItemBlueprint;
import me.adamix.mercury.core.MercuryCorePlugin;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.util.*;

public class CoreItemBlueprintManager implements ItemBlueprintManager {
	private final Map<Key, MercuryItemBlueprint> itemBlueprintMap = new HashMap<>();

	@Override
	public void registerAllItemBlueprints(@NotNull Path path) {
		List<CoreMercuryItemBlueprint> resultList = ItemBlueprintParser.parseAll(path);
		for (CoreMercuryItemBlueprint result : resultList) {
			registerBlueprint(result);
		}
	}

	@Override
	public void unloadBlueprints() {
		itemBlueprintMap.clear();
	}

	@Override
	public void registerBlueprint(@NotNull MercuryItemBlueprint item) {
		itemBlueprintMap.put(item.blueprintKey(), item);
		MercuryCorePlugin.getCoreLogger().info("Registered item blueprint: {}", item.blueprintKey());
	}

	@Override
	public @Nullable MercuryItemBlueprint getBlueprint(Key blueprintKey) {
		return itemBlueprintMap.get(blueprintKey);
	}

	@Override
	public @NotNull Set<Key> getItemIdSet() {
		return itemBlueprintMap.keySet();
	}
}
