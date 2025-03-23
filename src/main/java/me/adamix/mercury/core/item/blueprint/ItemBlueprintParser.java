package me.adamix.mercury.core.item.blueprint;

import me.adamix.mercury.core.MercuryCorePlugin;
import me.adamix.mercury.core.attribute.MercuryAttribute;
import me.adamix.mercury.core.attribute.MercuryAttributeModifier;
import me.adamix.mercury.core.item.component.ItemAttributeComponent;
import me.adamix.mercury.core.item.component.ItemDescriptionComponent;
import me.adamix.mercury.core.item.component.MercuryItemComponent;
import me.adamix.mercury.core.item.rarity.ItemRarity;
import me.adamix.mercury.core.toml.MercuryConfiguration;
import me.adamix.mercury.core.toml.MercuryTable;
import me.adamix.mercury.core.toml.exception.MissingTomlPropertyException;
import me.adamix.mercury.core.utils.TomlUtils;
import net.kyori.adventure.key.Key;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ItemBlueprintParser {
	public static @NotNull MercuryItemBlueprint parse(@NotNull File tomlFile) throws FileNotFoundException {
		if (!tomlFile.exists()) {
			throw new FileNotFoundException("Unable to parse item blueprint! File does not exist");
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
		MercuryTable attributeTable = toml.getTable("attributes");

		if (attributeTable != null) {
			Map<MercuryAttribute, MercuryAttributeModifier> modifierMap = TomlUtils.parseAttributes(attributeTable, key);

			componentList.add(
					new ItemAttributeComponent(modifierMap)
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

		return new MercuryItemBlueprint(
				key,
				material,
				name,
				componentList.toArray(new MercuryItemComponent[0])
		);
	}

	public static @NotNull List<MercuryItemBlueprint> parseAll(@NotNull String directory) {
		List<MercuryItemBlueprint> list = new ArrayList<>();

		File folder = new File(directory);
		if (!folder.exists() || !folder.isDirectory()) {
			return list;
		}

		File[] files = folder.listFiles((file) -> file.isFile() && file.getName().endsWith(".toml"));
		if (files == null) {
			return list;
		}

		for (File file : files) {
			try {
				list.add(parse(file));
			} catch (FileNotFoundException ignored) {
			} catch (RuntimeException e) {
				MercuryCorePlugin.getCoreLogger().error("Error while parsing {}: {}", file.getName(), e.getMessage());
			}

		}

		return list;
	}
}
