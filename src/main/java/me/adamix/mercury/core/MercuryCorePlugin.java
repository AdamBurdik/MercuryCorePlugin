package me.adamix.mercury.core;

import com.marcusslover.plus.lib.command.CommandManager;
import me.adamix.mercury.core.command.ItemCommand;
import me.adamix.mercury.core.command.SpawnCommand;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.java.JavaPlugin;

public class MercuryCorePlugin extends JavaPlugin {
	private static MercuryCorePlugin instance;

	@Override
	public void onEnable() {
		instance = this;
		MercuryCore.load(this);

		// ToDo Move command registration to different place. Maybe using MercuryCore
		new CommandManager(this).register(new ItemCommand()).register(new SpawnCommand());

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

	@Override
	public void onDisable() {
		MercuryCore.unload();
	}

	public static ComponentLogger getCoreLogger() {
		return instance.getComponentLogger();
	}

	public static String getFolderPath() {
		return instance.getDataPath().toAbsolutePath().toString();
	}
}