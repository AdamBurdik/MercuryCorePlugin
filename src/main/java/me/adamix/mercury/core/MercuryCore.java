package me.adamix.mercury.core;

import lombok.NonNull;
import me.adamix.mercury.core.configuration.defaults.PlayerDefaults;
import me.adamix.mercury.core.item.ItemManager;
import me.adamix.mercury.core.item.blueprint.ItemBlueprintManager;
import me.adamix.mercury.core.player.MercuryPlayer;
import me.adamix.mercury.core.translation.Translation;
import me.adamix.mercury.core.translation.TranslationManager;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

public class MercuryCore {
	private static MercuryCorePlugin plugin;
	private static TranslationManager translationManager;
	private static ItemBlueprintManager blueprintManager;
	private static ItemManager itemManager;

	/**
	 * Loads all necessary data for core plugin.
	 * <br>
	 * Should be called only from core plugin
	 * @param plugin mercury core plugin
	 */
	@ApiStatus.Internal
	public static void load(MercuryCorePlugin plugin) {
		MercuryCore.plugin = plugin;

		PlayerDefaults.load(MercuryCorePlugin.getFolderPath());

		translationManager = new TranslationManager();
		translationManager.loadAllTranslations(MercuryCorePlugin.getFolderPath());

		blueprintManager = new ItemBlueprintManager();
		blueprintManager.loadAllItems(MercuryCorePlugin.getFolderPath());
		itemManager = new ItemManager();
	}

	/**
	 * Unloads all loaded data from core plugin.
	 * <br>
	 * Should be called only from core plugin.
	 */
	@ApiStatus.Internal
	public static void unload() {
		translationManager.unloadTranslations();
		blueprintManager.unloadAllItems();
	}

	/**
	 * Retrieves player translation.
	 * @param player player to get translation from
	 * @return {@link Translation} instance
	 */
	public static @NotNull Translation getPlayerTranslation(@NotNull MercuryPlayer player) {
		Translation translation = translationManager.getTranslation(player.getTranslationId());
		if (translation == null) {
			// This should never be executed. Default translation should be always present. If not server won't start in the first place.
			throw new RuntimeException("Unable to get translation of player " + player.name() + " with translation id: " + player.getTranslationId() + "!");
		}

		return translation;
	}

	/**
	 * Retrieves translation manager instance.
	 * <br>
	 * Shouldn't be used outside of core plugin.
	 * @return {@link TranslationManager} instance.
	 */
	@ApiStatus.Internal
	public static TranslationManager translationManager() {
		return translationManager;
	}

	@ApiStatus.Internal
	public static ItemBlueprintManager itemBlueprintManager() {
		return blueprintManager;
	}

	@ApiStatus.Internal
	public static ItemManager itemManager() {
		return itemManager;
	}

	public static void stopServer(@NonNull String reason) {
		plugin.getComponentLogger().error("MercuryCore stopped the server! Reason: {}!", reason);
		plugin.getServer().shutdown();
	}

	public static NamespacedKey namespacedKey(@NotNull String value) {
		return new NamespacedKey(plugin, value);
	}
}
