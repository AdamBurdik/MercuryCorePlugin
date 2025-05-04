package me.adamix.mercury.core.item.blueprint;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import me.adamix.mercury.api.configuration.MercuryConfiguration;
import me.adamix.mercury.api.configuration.MercuryTable;
import me.adamix.mercury.api.item.component.MercuryItemComponent;
import me.adamix.mercury.common.configuration.toml.TomlConfiguration;
import me.adamix.mercury.core.MercuryCorePlugin;
import me.adamix.mercury.core.attribute.CoreAttributeContainer;
import net.kyori.adventure.key.Key;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ItemBlueprintParser {
	public static @NotNull CoreMercuryItemBlueprint parse(@NotNull File tomlFile) throws FileNotFoundException {
		if (!tomlFile.exists()) {
			throw new FileNotFoundException("Unable to parse item blueprint! File does not exist");
		}


		MercuryConfiguration toml = TomlConfiguration.create(tomlFile);
		@NotNull Key key = toml.getKeySafe("id");
		@NotNull String name = toml.getStringSafe("name");
		@NotNull Material material = toml.getMaterialSafe("material");

		Set<MercuryItemComponent> components = new ObjectOpenHashSet<>();

		// Parse attributes to component
		MercuryTable attributeTable = toml.getTable("attributes");
		// ToDO Parse attributes

		return new CoreMercuryItemBlueprint(
				key,
				material,
				name,
				new CoreAttributeContainer(),
				components.toArray(new MercuryItemComponent[0])
		);
	}

	public static @NotNull List<CoreMercuryItemBlueprint> parseAll(@NotNull Path path) {
		List<CoreMercuryItemBlueprint> list = new ArrayList<>();

		File folder = path.toFile();
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
