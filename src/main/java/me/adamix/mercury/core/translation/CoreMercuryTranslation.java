package me.adamix.mercury.core.translation;

import me.adamix.mercury.api.translation.MercuryTranslation;
import me.adamix.mercury.core.MercuryCorePlugin;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CoreMercuryTranslation implements MercuryTranslation {
	private final @NotNull String translationCode;
	private final @NotNull Map<String, String> translationMap = new HashMap<>();

	public CoreMercuryTranslation(@NotNull String translationCode, @NotNull Set<Map.Entry<String, Object>> dottedEntrySet) {
		this.translationCode = translationCode;
		for (Map.Entry<String, Object> entry : dottedEntrySet) {
			String key = entry.getKey();
			Object value = entry.getValue();

			translationMap.put(key, String.valueOf(value));
		}
	}

	/**
	 * Retrieves translation by key as string
	 * @param dottedKey key to get translation by
	 * @return translation {@link String}
	 */
	public @NotNull String get(@NotNull String dottedKey) {
		if (!translationMap.containsKey(dottedKey)) {
			MercuryCorePlugin.getCoreLogger().error("Unable to get translation for key: {}:{}!", this.translationCode, dottedKey);
			return translationCode + ":" + dottedKey;
		}
		return translationMap.get(dottedKey);
	}

	/**
	 * Retrieves translation by key as component
	 * @param dottedKey key to get translation by
	 * @return translation {@link Component}
	 */
	public @NotNull Component getComponent(@NotNull String dottedKey) {
		return Component.text(this.get(dottedKey));
	}
}
