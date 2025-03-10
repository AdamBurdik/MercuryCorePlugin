package me.adamix.mercury.core.translation;

import me.adamix.mercury.core.MercuryCore;
import me.adamix.mercury.core.MercuryCorePlugin;
import me.adamix.mercury.core.configuration.defaults.PlayerDefaults;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.tomlj.Toml;
import org.tomlj.TomlParseResult;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TranslationManager {
	private final Map<String, Translation> translationMap = new HashMap<>();

	public void unloadTranslations() {
		translationMap.clear();
	}

	/**
	 * Load and save translation from file
	 *
	 * @param fileName Translation file name including .toml extension
	 */
	public void loadTranslation(@NotNull File translationFile) {
		TomlParseResult result = null;
		try {
			result = Toml.parse(translationFile.toPath());
		} catch (IOException e) {
			MercuryCorePlugin.getCoreLogger().error("Error while parsing translation: {}\n{}\n", translationFile.getName(), e.toString());
		}
		if (result == null) {
			return;
		}

		if (result.hasErrors()) {
			result.errors().forEach(error -> MercuryCorePlugin.getCoreLogger().error("Error in {} translation! {}", translationFile.getName(), error.toString()));
			return;
		}

		String translationId = result.getString("translation.id");
		if (translationId == null) {
			// Ignore toml files that does not contain translation.id.
			return;
		}

		Translation translation = new Translation(translationId, result.dottedEntrySet());
		translationMap.put(
				translationId,
				translation
		);
		MercuryCorePlugin.getCoreLogger().info("Successfully loaded {} translation.", translation.get("translation.name"));
	}

	/**
	 * Retrieves translation by translation id
	 *
	 * @param translationId Translation id. This is configured in each translation file.
	 * @return - Translation instance, or default translation if id translationId is null. If default translation is also null, returns null
	 */
	public @Nullable Translation getTranslation(@Nullable String translationId) {
		if (translationId == null) {
			translationId = PlayerDefaults.getTranslationId();
		}
		if (!translationMap.containsKey(translationId)) {
			return translationMap.get("en");
		}
		return translationMap.get(translationId);
	}

	public void loadAllTranslations(@NotNull String folderPath) {
		File translationFolder = new File(folderPath + "/translations/");
		if (!translationFolder.exists()) {
			translationFolder.mkdirs();
		}

		File[] files = translationFolder.listFiles((file) -> file.isFile() && file.getName().endsWith(".toml"));
		if (files == null) {
			MercuryCorePlugin.getCoreLogger().error("No translation files has been found in {}! Please provide at least english!", translationFolder.getAbsolutePath());
			MercuryCore.stopServer("No translation files has been found in " + translationFolder.getAbsolutePath());
			return;
		}
		for (File file : files) {
			loadTranslation(file);
		}

		// Stop server if player default translation is missing.
		if (getTranslation(PlayerDefaults.getTranslationId()) == null) {
			MercuryCorePlugin.getCoreLogger().error("Default translation for player is set to {} but it does not exist!", PlayerDefaults.getTranslationId());
			MercuryCore.stopServer("Default translation for player is set to " + PlayerDefaults.getTranslationId() + " but it does not exist");
		}
	}
}
