package me.adamix.mercury.core;

import com.marcusslover.plus.lib.command.CommandManager;
import me.adamix.mercury.core.command.ItemCommand;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.bukkit.plugin.java.JavaPlugin;

public class MercuryCorePlugin extends JavaPlugin {
	private static MercuryCorePlugin instance;

	@Override
	public void onEnable() {
		instance = this;
		MercuryCore.load(this);

		// ToDo Move command registration to different place. Maybe using MercuryCore
		new CommandManager(this).register(new ItemCommand());
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