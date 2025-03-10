package me.adamix.mercury.core;

import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.bukkit.plugin.java.JavaPlugin;

public class MercuryCorePlugin extends JavaPlugin {
	private static MercuryCorePlugin instance;

	@Override
	public void onEnable() {
		instance = this;
	}

	@Override
	public void onDisable() {

	}

	public static ComponentLogger getCoreLogger() {
		return instance.getComponentLogger();
	}

	public static String getFolderPath() {
		return instance.getDataPath().toAbsolutePath().toString();
	}
}