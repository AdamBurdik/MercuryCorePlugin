package me.adamix.mercury.core.entity.blueprint;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import me.adamix.mercury.api.attribute.MercuryAttribute;
import me.adamix.mercury.api.configuration.MercuryConfiguration;
import me.adamix.mercury.api.configuration.MercuryTable;
import me.adamix.mercury.api.entity.blueprint.MercuryEntityBlueprint;
import me.adamix.mercury.api.entity.component.MercuryEntityComponent;
import me.adamix.mercury.api.entity.type.MercuryEntityType;
import me.adamix.mercury.common.configuration.toml.TomlConfiguration;
import me.adamix.mercury.core.MercuryCorePlugin;
import me.adamix.mercury.core.attribute.CoreAttributeContainer;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Responsible for parsing toml files to blueprint.
 */
public class EntityBlueprintParser {
	public static @NotNull MercuryEntityBlueprint parse(@NotNull File tomlFile) throws FileNotFoundException {
		if (!tomlFile.exists()) {
			throw new FileNotFoundException("Unable to parse entity blueprint! File does not exist");
		}

		MercuryConfiguration toml = TomlConfiguration.create(tomlFile);
		Key key = toml.getKeySafe("id");
		String stringType = toml.getStringSafe("type");
		MercuryEntityType type = MercuryEntityType.valueOf(stringType);

		long health= toml.getLongSafe("health");
		long maxHealth= toml.getLongSafe("max_health");
		long flags = toml.getLongSafe("flags");


		Set<MercuryEntityComponent> components = new ObjectOpenHashSet<>();

		CoreAttributeContainer mobAttributeContainer = new CoreAttributeContainer();

		// Parse attributes to component
		MercuryTable attributeTable = toml.getTable("attributes");
		if (attributeTable != null) {
			mobAttributeContainer
					// .set method can accept nullable.
					// ToDO Automatically set all available attributes
					.set(MercuryAttribute.DAMAGE, attributeTable.getDouble("damage"))
					.set(MercuryAttribute.MOVEMENT_SPEED, attributeTable.getDouble("movement_speed"))
					.set(MercuryAttribute.ATTACK_SPEED, attributeTable.getDouble("attack_speed"))
					.set(MercuryAttribute.MAX_HEALTH, attributeTable.getDouble("max_health"));
		}

		return new CoreEntityBlueprint(
				key,
				type,
				health,
				maxHealth,
				flags,
				components.toArray(new MercuryEntityComponent[0]),
				mobAttributeContainer
		);
	}

	/**
	 * Parses all available entities in specified directory.
	 * @param path Path of directory.
	 * @return {@link List} of all parsed blueprints. Can be empty.
	 */
	public static @NotNull List<MercuryEntityBlueprint> parseAll(@NotNull Path path) {
		List<MercuryEntityBlueprint> list = new ArrayList<>();

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
