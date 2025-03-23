package me.adamix.mercury.core;

import com.marcusslover.plus.lib.command.CommandManager;
import me.adamix.mercury.core.command.ItemCommand;
import me.adamix.mercury.core.command.MobCommand;
import me.adamix.mercury.core.command.types.ItemBlueprintParameterType;
import me.adamix.mercury.core.command.types.MobBlueprintParameterType;
import me.adamix.mercury.core.item.blueprint.MercuryItemBlueprint;
import me.adamix.mercury.core.listener.entity.EntityEventListener;
import me.adamix.mercury.core.listener.player.PlayerEventListener;
import me.adamix.mercury.core.mob.blueprint.MercuryMobBlueprint;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.java.JavaPlugin;
import revxrsal.commands.Lamp;
import revxrsal.commands.bukkit.BukkitLamp;
import revxrsal.commands.bukkit.actor.BukkitCommandActor;

public class MercuryCorePlugin extends JavaPlugin {
	private static MercuryCorePlugin instance;

	@Override
	public void onEnable() {
		instance = this;
		MercuryCore.load(this);

		// ToDO Move event listener registration to different place. Maybe using MercuryCore
		Bukkit.getPluginManager().registerEvents(new EntityEventListener(), this);
		Bukkit.getPluginManager().registerEvents(new PlayerEventListener(), this);

		removeCommands();
		registerCommands();
	}

	@Override
	public void onDisable() {
		MercuryCore.unload();
	}

	public void removeCommands() {
		// ToDo Make something better
		// Temporary solution for removing vanilla commands
		CommandMap commandMap = Bukkit.getCommandMap();
		var knownCommands = commandMap.getKnownCommands();
		for (String disabledCommand : MercuryCore.coreConfiguration().getArray("disabled_commands").toStringArray()) {
			Command command = commandMap.getCommand(disabledCommand);
			if (command != null) {
				command.unregister(commandMap);
				knownCommands.remove(disabledCommand);
			}
		}
	}

	public void registerCommands() {
		Lamp<BukkitCommandActor> lamp = BukkitLamp.builder(this)
				.parameterTypes(builder -> {
					builder.addParameterType(MercuryItemBlueprint.class, new ItemBlueprintParameterType());
					builder.addParameterType(MercuryMobBlueprint.class, new MobBlueprintParameterType());
				})
				.build();

		lamp.register(new ItemCommand());
		lamp.register(new MobCommand());
	}

	public static ComponentLogger getCoreLogger() {
		return instance.getComponentLogger();
	}

	public static String getFolderPath() {
		return instance.getDataPath().toAbsolutePath().toString();
	}
}