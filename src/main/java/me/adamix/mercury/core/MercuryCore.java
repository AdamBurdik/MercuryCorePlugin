package me.adamix.mercury.core;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import lombok.NonNull;
import me.adamix.mercury.core.configuration.defaults.PlayerDefaults;
import me.adamix.mercury.core.item.ItemManager;
import me.adamix.mercury.core.item.blueprint.ItemBlueprintManager;
import me.adamix.mercury.core.mob.DummyMobBlueprint;
import me.adamix.mercury.core.mob.MercuryMob;
import me.adamix.mercury.core.mob.MobManager;
import me.adamix.mercury.core.placeholder.PlaceholderManager;
import me.adamix.mercury.core.player.MercuryPlayer;
import me.adamix.mercury.core.player.PlayerManager;
import me.adamix.mercury.core.protocol.MercuryProtocol;
import me.adamix.mercury.core.toml.MercuryConfiguration;
import me.adamix.mercury.core.translation.Translation;
import me.adamix.mercury.core.translation.TranslationManager;
import net.kyori.adventure.key.Key;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.UUID;


public class MercuryCore {
	private static MercuryCorePlugin plugin;
	private static MercuryConfiguration coreConfiguration;
	private static TranslationManager translationManager;
	private static ItemBlueprintManager blueprintManager;
	private static ItemManager itemManager;
	private static PlaceholderManager placeholderManager;
	private static MobManager mobManager;
	private static PlayerManager playerManager;
	private static MercuryProtocol protocol;

	/**
	 * Loads all necessary data for core plugin.
	 * <br>
	 * Should be called only from core plugin
	 * @param plugin mercury core plugin
	 */
	@ApiStatus.Internal
	public static void load(MercuryCorePlugin plugin) {
		MercuryCore.plugin = plugin;

		// Create config.toml
		File file = new File(MercuryCorePlugin.getFolderPath() + "/config.toml");
		if (!file.exists()) {
			plugin.saveResource("config.toml", false);
		}
		coreConfiguration = new MercuryConfiguration(file);

		PlayerDefaults.load(MercuryCorePlugin.getFolderPath());

		playerManager = new PlayerManager();
		translationManager = new TranslationManager();
		translationManager.loadAllTranslations(MercuryCorePlugin.getFolderPath());
		blueprintManager = new ItemBlueprintManager();
		blueprintManager.loadAllItems(MercuryCorePlugin.getFolderPath());
		itemManager = new ItemManager();
		placeholderManager = new PlaceholderManager();
		mobManager = new MobManager();
		mobManager.registerAllEntityBlueprints(MercuryCorePlugin.getFolderPath());
		mobManager.registerBlueprint(Key.key("mercury", "test"), new DummyMobBlueprint());

		ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
		protocol = new MercuryProtocol(protocolManager);
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
	 * Spawns a mob in world at specified location
	 * @param mob mob to spawn
	 * @param location location where mob should be spawned
	 */
	public static void spawnMob(@NotNull MercuryMob mob, @NotNull Location location) {
		mobManager.spawn(mob, location);
	}

	/**
	 * Retrieves mercury player instance from uuid.
	 * @param uuid player uuid.
	 * @return {@link MercuryPlayer} instance or null if player is not online or does not exist.
	 */
	public static @Nullable MercuryPlayer getPlayer(@NotNull UUID uuid) {
		return playerManager.getPlayer(uuid);
	}

	/**
	 * Retrieves translation manager instance.
	 * <br>
	 * @return {@link TranslationManager} instance.
	 */
	@ApiStatus.Internal
	public static TranslationManager translationManager() {
		return translationManager;
	}

	/**
	 * Retrieves item blueprint manager instance.
	 * <br>
	 * @return {@link ItemBlueprintManager} instance.
	 */
	@ApiStatus.Internal
	public static ItemBlueprintManager itemBlueprintManager() {
		return blueprintManager;
	}

	/**
	 * Retrieves item manager instance.
	 * <br>
	 * @return {@link ItemManager} instance.
	 */
	@ApiStatus.Internal
	public static ItemManager itemManager() {
		return itemManager;
	}

	/**
	 * Retrieves placeholder manager instance.
	 * <br>
	 * @return {@link PlaceholderManager} instance.
	 */
	@ApiStatus.Internal
	public static PlaceholderManager placeholderManager() {
		return placeholderManager;
	}

	/**
	 * Retrieves mob manager instance.
	 * @return {@link MobManager} instance.
	 */
	@ApiStatus.Internal
	public static MobManager mobManager() {
		return mobManager;
	}

	/**
	 * Retrieves player manager instance.
	 * @return {@link PlayerManager} instance.
	 */
	@ApiStatus.Internal
	public static PlayerManager playerManager() {
		return playerManager;
	}

	/**
	 * Retrieves instance of mercury protocol.
	 * @return {@link MercuryProtocol} instance.
	 */
	@ApiStatus.Internal
	public static MercuryProtocol protocol() {
		return protocol;
	}

	/**
	 * Retrieves configuration of core plugin.
	 * <br>
	 * @return {@link MercuryConfiguration} instance.
	 */
	@ApiStatus.Internal
	public static MercuryConfiguration coreConfiguration() {
		return coreConfiguration;
	}

	/**
	 * Stops the server with specified reason.
	 * <br>
	 * @param reason reason why server was stopped.
	 */
	public static void stopServer(@NonNull String reason) {
		plugin.getComponentLogger().error("MercuryCore stopped the server! Reason: {}!", reason);
		plugin.getServer().shutdown();
	}

	/**
	 * Creates namespacedKey with core plugin as namespace.
	 * @param value value of namespacedKey
	 * @return {@link NamespacedKey} instance.
	 */
	public static NamespacedKey namespacedKey(@NotNull String value) {
		return new NamespacedKey(plugin, value);
	}
}
