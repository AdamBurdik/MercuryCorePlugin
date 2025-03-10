package me.adamix.mercury.core.item.blueprint;

import me.adamix.mercury.core.MercuryCorePlugin;
import me.adamix.mercury.core.attribute.AttributeContainer;
import me.adamix.mercury.core.attribute.MercuryAttribute;
import me.adamix.mercury.core.item.component.ItemAttributeComponent;
import me.adamix.mercury.core.item.component.ItemDescriptionComponent;
import me.adamix.mercury.core.item.component.MercuryItemComponent;
import me.adamix.mercury.core.item.rarity.ItemRarity;
import me.adamix.mercury.core.toml.MercuryConfiguration;
import me.adamix.mercury.core.utils.FileUtils;
import me.adamix.mercury.core.utils.TomlUtils;
import net.kyori.adventure.key.Key;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tomlj.TomlTable;

import java.io.File;
import java.util.*;

public class ItemBlueprintManager {
	private static final Logger LOGGER = LoggerFactory.getLogger(ItemBlueprintManager.class);
	private final Map<Key, MercuryItemBlueprint> itemBlueprintMap = new HashMap<>();

	public void loadAllItems(@NotNull String folderPath) {
		File itemDirectory = new File(folderPath + "/items/");
		List<File> fileList = FileUtils.getAllFiles(itemDirectory);

		for (File file : fileList) {
			String extension = FileUtils.getExtension(file);
			if (!extension.equals("toml")) {
				continue;
			}

			register(file);
		}
	}

	public void unloadAllItems() {
		itemBlueprintMap.clear();
	}

	public void register(@NotNull File tomlFile) {
		if (!tomlFile.exists()) {
			throw new RuntimeException("Unable to register item! File does not exist");
		}

		MercuryConfiguration toml = new MercuryConfiguration(tomlFile);

		@NotNull Key key = toml.getKeySafe("id");
		@NotNull String name = toml.getStringSafe("name");
		@NotNull Material material = toml.getMaterialSafe("material");

		List<MercuryItemComponent> componentList = new ArrayList<>();

		// Parse description to component
		String description = toml.getString("description");
		if (description != null) {
			String[] lines = description.split("\n");

			componentList.add(
					new ItemDescriptionComponent(lines)
			);
		}

		// Parse attributes to component
		TomlTable attributeTable = toml.getTomlTable("attributes");
		if (attributeTable != null) {
			AttributeContainer attributeContainer = new AttributeContainer();
			attributeContainer
					.set(MercuryAttribute.DAMAGE, TomlUtils.parseAttribute(attributeTable, "damage"))
					.set(MercuryAttribute.MOVEMENT_SPEED, TomlUtils.parseAttribute(attributeTable, "movement_speed"))
					.set(MercuryAttribute.ATTACK_SPEED, TomlUtils.parseAttribute(attributeTable, "attack_speed"))
					.set(MercuryAttribute.MAX_HEALTH, TomlUtils.parseAttribute(attributeTable, "max_health"));
			componentList.add(
					new ItemAttributeComponent(attributeContainer.getAttributeMap())
			);
		}

		// Parse rarity to component
		String rarity = toml.getString("rarity");
		if (rarity != null) {
			try {
				ItemRarity itemRarity = ItemRarity.valueOf(rarity.toUpperCase());
				componentList.add(itemRarity.toComponent());
			} catch (IllegalArgumentException e) {
				throw new RuntimeException(
						String.format("Unknown rarity %s in %s (%s) configuration! Please specify valid rarity. e.g. 'common'", rarity, key, tomlFile.getName())
				);
			}
		}


		LOGGER.info("Item '{}' ({}) has been registered", key, tomlFile.getName());
		MercuryItemBlueprint item = new MercuryItemBlueprint(
				key,
				material,
				name,
				componentList.toArray(new MercuryItemComponent[0])
		);

		this.register(item);

	}

	public void register(MercuryItemBlueprint item) {
		itemBlueprintMap.put(item.blueprintKey(), item);
	}

	public @NotNull Optional<MercuryItemBlueprint> get(Key blueprintKey) {
		MercuryCorePlugin.getCoreLogger().info(String.valueOf(itemBlueprintMap));
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
