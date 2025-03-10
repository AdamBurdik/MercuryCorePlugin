package me.adamix.mercury.core;

import lombok.NonNull;
import org.jetbrains.annotations.ApiStatus;

public class MercuryCore {
	private static MercuryCorePlugin plugin;

	/**
	 * Loads all necessary data for core plugin.
	 * <br>
	 * Should be called only from core plugin
	 * @param plugin mercury core plugin
	 */
	@ApiStatus.Internal
	public static void load(MercuryCorePlugin plugin) {
		MercuryCore.plugin = plugin;
	}

	/**
	 * Unloads all loaded data from core plugin.
	 * <br>
	 * Should be called only from core plugin.
	 */
	@ApiStatus.Internal
	public static void unload() {
	}


	public static void stopServer(@NonNull String reason) {
		plugin.getComponentLogger().error("MercuryCore stopped the server! Reason: {}!", reason);
		plugin.getServer().shutdown();
	}
}
