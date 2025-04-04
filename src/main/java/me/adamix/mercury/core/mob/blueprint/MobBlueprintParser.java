package me.adamix.mercury.core.mob.blueprint;

import me.adamix.mercury.core.MercuryCorePlugin;
import me.adamix.mercury.core.attribute.MercuryAttribute;
import me.adamix.mercury.core.mob.attribute.MobAttributeContainer;
import me.adamix.mercury.core.mob.component.MercuryMobComponent;
import me.adamix.mercury.core.toml.MercuryConfiguration;
import me.adamix.mercury.core.toml.exception.MissingTomlPropertyException;
import net.kyori.adventure.key.Key;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.tomlj.TomlTable;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * Responsible for parsing toml files to blueprint.
 */
public class MobBlueprintParser {
	public static @NotNull MobBlueprintParseResult parse(@NotNull File tomlFile) throws FileNotFoundException {
		if (!tomlFile.exists()) {
			throw new FileNotFoundException("Unable to parse entity blueprint! File does not exist");
		}

		MercuryConfiguration toml = new MercuryConfiguration(tomlFile);
		Key mobKey = toml.getKeySafe("id");
		String stringType = toml.getStringSafe("type");
		EntityType type = EntityType.fromName(stringType);
		if (type == null) {
			throw new RuntimeException("Unable to parse entity blueprint! Type " + stringType + " does not exist!");
		}
		String name = toml.getStringSafe("name");


		List<MercuryMobComponent> componentList = new ArrayList<>();

		// Parse attributes to component
		TomlTable attributeTable = toml.getTomlTable("attributes");
		if (attributeTable != null) {
			MobAttributeContainer mobAttributeContainer = new MobAttributeContainer();
			mobAttributeContainer
					// .set method can accept nullable.
					// ToDO Automatically set all available attributes
					.set(MercuryAttribute.DAMAGE, attributeTable.getDouble("damage"))
					.set(MercuryAttribute.MOVEMENT_SPEED, attributeTable.getDouble("movement_speed"))
					.set(MercuryAttribute.ATTACK_SPEED, attributeTable.getDouble("attack_speed"))
					.set(MercuryAttribute.MAX_HEALTH, attributeTable.getDouble("max_health"));
			componentList.add(
					mobAttributeContainer.toComponent()
			);
		}

		return new MobBlueprintParseResult(
				mobKey,
				new MercuryMobBlueprint(
						type,
						name,
						componentList.toArray(new MercuryMobComponent[0]),
						null
				)
		);
	}

	/**
	 * Parses all available entities in specified directory.
	 * @param directory directory location
	 * @return {@link List} of all parsed blueprints. Can be empty.
	 */
	public static @NotNull List<MobBlueprintParseResult> parseAll(@NotNull String directory) {
		List<MobBlueprintParseResult> list = new ArrayList<>();

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

	public record MobBlueprintParseResult(Key mobKey, MercuryMobBlueprint blueprint) {}
}
