package me.adamix.mercury.core.configuration.defaults;

import lombok.Getter;
import me.adamix.mercury.api.configuration.MercuryConfiguration;
import me.adamix.mercury.common.configuration.toml.TomlConfiguration;
import me.adamix.mercury.core.MercuryCorePlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

/**
 * Represents default data for most of the configurable stuff.
 * <br>
 * Data is loaded from /defaults/player.toml.
 */
public class PlayerDefaults {
	@Getter
	private static @NotNull String translationId = "en";

	public static void load(@NotNull String folderPath) {
		File file = new File(folderPath + "defaults/player.toml");
		if (!file.exists()) {
			MercuryCorePlugin.getCoreLogger().warn("Player default configuration file does not exist! Default hard-coded values will be used instead!");
			return;
		}
		MercuryConfiguration configuration = TomlConfiguration.create(file);

		@Nullable String rawTranslationId = configuration.getString("translation_id");
		if (rawTranslationId != null) {
			translationId = rawTranslationId;
		}
	}
}
