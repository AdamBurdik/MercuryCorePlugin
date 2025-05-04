package me.adamix.mercury.core;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import lombok.Getter;
import lombok.NonNull;
import me.adamix.mercury.api.MercuryAPI;
import me.adamix.mercury.api.MercuryCore;
import me.adamix.mercury.api.builders.MercuryBuilderFactory;
import me.adamix.mercury.api.configuration.MercuryConfiguration;
import me.adamix.mercury.api.data.DataManager;
import me.adamix.mercury.api.entity.EntityManager;
import me.adamix.mercury.api.entity.blueprint.EntityBlueprintManager;
import me.adamix.mercury.api.item.ItemManager;
import me.adamix.mercury.api.item.blueprint.ItemBlueprintManager;
import me.adamix.mercury.api.placeholder.PlaceholderManager;
import me.adamix.mercury.api.protocol.MercuryProtocol;
import me.adamix.mercury.common.configuration.toml.TomlConfiguration;
import me.adamix.mercury.core.configuration.defaults.PlayerDefaults;
import me.adamix.mercury.core.data.*;
import me.adamix.mercury.core.entity.CoreEntityManager;
import me.adamix.mercury.core.entity.blueprint.CoreEntityBlueprintManager;
import me.adamix.mercury.core.event.EventNode;
import me.adamix.mercury.core.event.MercuryEvent;
import me.adamix.mercury.core.item.CoreItemManager;
import me.adamix.mercury.core.item.blueprint.CoreItemBlueprintManager;
import me.adamix.mercury.core.placeholder.CorePlaceholderManager;
import me.adamix.mercury.core.player.CorePlayerManager;
import me.adamix.mercury.core.protocol.CoreMercuryProtocol;
import me.adamix.mercury.core.script.ScriptManager;
import me.adamix.mercury.core.translation.CoreTranslationManager;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.nio.file.Path;


@SuppressWarnings("UnstableApiUsage")
public class MercuryCoreImpl extends MercuryCore {
	@Getter
	private final EventNode<MercuryEvent> globalEventNode = EventNode.all("global");
	private final MercuryCorePlugin plugin;
	private MercuryConfiguration coreConfiguration;
	private CoreTranslationManager translationManager;
	private CoreItemManager itemManager;
	private CoreItemBlueprintManager itemBlueprintManager;
	private CorePlaceholderManager placeholderManager;
	private CoreEntityManager entityManager;
	private CoreEntityBlueprintManager entityBlueprintManager;
	private CorePlayerManager playerManager;
	private CoreMercuryProtocol protocol;
	private CoreDataManager dataManager;
	private ScriptManager scriptManager;

	public MercuryCoreImpl(MercuryCorePlugin plugin) {
		this.plugin = plugin;
	}

	@Override
	public void load(@NotNull MercuryAPI api) {
		// Create config.toml
		File file = new File(MercuryCorePlugin.getFolderPath() + "/config.toml");
		if (!file.exists()) {
			plugin.saveResource("config.toml", false);
		}
		coreConfiguration = TomlConfiguration.create(file);

		PlayerDefaults.load(MercuryCorePlugin.getFolderPath());

		playerManager = new CorePlayerManager();
		translationManager = new CoreTranslationManager();
		translationManager.loadAllTranslations(Path.of(MercuryCorePlugin.getFolderPath()));
		itemBlueprintManager = new CoreItemBlueprintManager();
		itemBlueprintManager.registerAllItemBlueprints(Path.of(MercuryCorePlugin.getFolderPath()).resolve("item"));
		itemManager = new CoreItemManager();
		placeholderManager = new CorePlaceholderManager();
		entityManager = new CoreEntityManager();
		entityBlueprintManager = new CoreEntityBlueprintManager();
		entityBlueprintManager.registerAllItemBlueprints(Path.of(MercuryCorePlugin.getFolderPath()).resolve("entity"));
		dataManager = new CoreDataManager(MercuryCorePlugin.getFolderPath() + coreConfiguration.getString("core_database_filename"));
		scriptManager = new ScriptManager();
		scriptManager.loadScripts(MercuryCorePlugin.getFolderPath() + "/scripts");

		ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
		protocol = new CoreMercuryProtocol(protocolManager);

		dataManager.registerDataHolder(new DummyDataHolder(), DummyData.class);
	}

	@Override
	public void unload() {
		translationManager.unloadTranslations();
		itemBlueprintManager.unloadBlueprints();
		entityBlueprintManager.unloadBlueprints();
		dataManager.close();
	}

	@Override
	public @NotNull MercuryBuilderFactory getBuilderFactory() {
		return null;
	}

	@Override
	public MercuryAPI corePlugin() {
		return plugin;
	}

	@Override
	public CoreTranslationManager getTranslationManager() {
		return translationManager;
	}

	@Override
	public CorePlayerManager getPlayerManager() {
		return playerManager;
	}

	@Override
	public DataManager getDataManager() {
		return dataManager;
	}

	@Override
	public PlaceholderManager getPlaceholderManager() {
		return placeholderManager;
	}

	@Override
	public MercuryProtocol getProtocol() {
		return protocol;
	}

	@Override
	public EntityManager getEntityManager() {
		return entityManager;
	}

	@Override
	public EntityBlueprintManager getEntityBlueprintManager() {
		return entityBlueprintManager;
	}

	@Override
	public ItemManager getItemManager() {
		return itemManager;
	}

	@Override
	public ItemBlueprintManager getItemBlueprintManager() {
		return itemBlueprintManager;
	}

	/**
	 * Stops the core plugin with specified reason.
	 * <br>
	 * @param reason reason why core plugin was stopped.
	 */
	public void stop(@NonNull String reason) {
		plugin.getComponentLogger().error("MercuryCoreImpl has been stopped! Reason: {}!", reason);
		Bukkit.getPluginManager().disablePlugin(plugin);
	}
}
