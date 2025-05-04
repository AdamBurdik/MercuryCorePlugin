package me.adamix.mercury.core.translation;

import me.adamix.mercury.api.configuration.MercuryConfiguration;
import me.adamix.mercury.api.translation.TranslationManager;
import me.adamix.mercury.common.configuration.toml.TomlConfiguration;
import me.adamix.mercury.core.MercuryCoreImpl;
import me.adamix.mercury.core.MercuryCorePlugin;
import me.adamix.mercury.core.configuration.defaults.PlayerDefaults;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class CoreTranslationManager implements TranslationManager {
	private final Map<String, CoreMercuryTranslation> translationMap = new HashMap<>();

	public void unloadTranslations() {
		translationMap.clear();
	}

	@Override
	public void loadTranslation(@NotNull File file) {
		MercuryConfiguration config = TomlConfiguration.create(file);

		String translationId = config.getString("translation.id");
		if (translationId == null) {
			// Ignore toml files that does not contain translation.id.
			return;
		}

		CoreMercuryTranslation translation = new CoreMercuryTranslation(translationId, config.dottedEntrySet(false));
		translationMap.put(
				translationId,
				translation
		);
		MercuryCorePlugin.getCoreLogger().info("Successfully loaded {} translation.", translation.get("translation.name"));
	}

	/**
	 * Retrieves translation by translation id
	 *
	 * @param translationId CoreMercuryTranslation id. This is configured in each translation file.
	 * @return - CoreMercuryTranslation instance, or default translation if id translationId is null. If default translation is also null, returns null
	 */
	public @Nullable CoreMercuryTranslation getTranslation(@Nullable String translationId) {
		if (translationId == null) {
			translationId = PlayerDefaults.getTranslationId();
		}
		if (!translationMap.containsKey(translationId)) {
			return translationMap.get("en");
		}
		return translationMap.get(translationId);
	}

	public void loadAllTranslations(@NotNull Path path) {
		File translationFolder = path.resolve("translations").toFile();
		if (!translationFolder.exists()) {
			translationFolder.mkdirs();
		}

		File[] files = translationFolder.listFiles((file) -> file.isFile() && file.getName().endsWith(".toml"));
		if (files == null) {
			MercuryCorePlugin.getCoreLogger().error("No translation files has been found in {}! Please provide at least english!", translationFolder.getAbsolutePath());
			((MercuryCoreImpl) MercuryCorePlugin.getInstance().getMercuryCore()).stop("No translation files has been found in " + translationFolder.getAbsolutePath());
			// ToDo Add stop method to api interface, or figure out better way to handle stopping the plugin
			return;
		}
		for (File file : files) {
			loadTranslation(file);
		}

		// Stop server if player default translation is missing.
		if (getTranslation(PlayerDefaults.getTranslationId()) == null) {
			MercuryCorePlugin.getCoreLogger().error("Default translation for player is set to {} but it does not exist!", PlayerDefaults.getTranslationId());
			// ToDo Add stop method to api interface, or figure out better way to handle stopping the plugin
			((MercuryCoreImpl) MercuryCorePlugin.getInstance().getMercuryCore()).stop("Default translation for player is set to " + PlayerDefaults.getTranslationId() + " but it does not exist");
		}
	}
}
