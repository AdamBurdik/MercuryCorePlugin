package me.adamix.mercury.core;

import me.adamix.mercury.api.MercuryAPI;
import me.adamix.mercury.api.MercuryCore;
import me.adamix.mercury.api.entity.blueprint.MercuryEntityBlueprint;
import me.adamix.mercury.api.item.blueprint.MercuryItemBlueprint;
import me.adamix.mercury.core.command.*;
import me.adamix.mercury.core.command.types.ItemBlueprintParameterType;
import me.adamix.mercury.core.command.types.EntityBlueprintParameterType;
import me.adamix.mercury.core.item.blueprint.CoreMercuryItemBlueprint;
import me.adamix.mercury.core.listener.command.CommandListener;
import me.adamix.mercury.core.listener.entity.EntityEventListener;
import me.adamix.mercury.core.listener.player.PlayerEventListener;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import revxrsal.commands.Lamp;
import revxrsal.commands.bukkit.BukkitLamp;
import revxrsal.commands.bukkit.actor.BukkitCommandActor;

public class MercuryCorePlugin extends JavaPlugin implements MercuryAPI {
	private static MercuryCorePlugin instance;
	private static MercuryCoreImpl coreInstance;

	public static MercuryCorePlugin getInstance() {
		return instance;
	}

	@Override
	public void onEnable() {
		instance = this;
		coreInstance = new MercuryCoreImpl(this);
		MercuryCore.setInstance(coreInstance);
		coreInstance.load(this);

		// ToDO Move event listener registration to different place. Maybe using MercuryCoreImpl
		Bukkit.getPluginManager().registerEvents(new EntityEventListener(), this);
		Bukkit.getPluginManager().registerEvents(new PlayerEventListener(), this);
		Bukkit.getPluginManager().registerEvents(new CommandListener(), this);
		getCommand("ui").setExecutor(new UICommand());

		removeCommands();
		registerCommands();
	}

	@Override
	public void onDisable() {
		coreInstance.unload();
	}

	public void removeCommands() {
		// ToDo Reimplement
	}

	public void registerCommands() {
		Lamp<BukkitCommandActor> lamp = BukkitLamp.builder(this)
				.parameterTypes(builder -> {
					builder.addParameterType(MercuryItemBlueprint.class, new ItemBlueprintParameterType());
					builder.addParameterType(MercuryEntityBlueprint.class, new EntityBlueprintParameterType());
				})
				.build();

		lamp.register(new ItemCommand());
		lamp.register(new EntityCommand());
		lamp.register(new TestCommand());
		lamp.register(new ScriptCommand());
	}

	public static ComponentLogger getCoreLogger() {
		return instance.getComponentLogger();
	}

	public static String getFolderPath() {
		return instance.getDataPath().toAbsolutePath().toString();
	}

	@Override
	public @NotNull MercuryCore getMercuryCore() {
		return coreInstance;
	}
}