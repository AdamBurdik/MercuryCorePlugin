package me.adamix.mercury.core.protocol;

import com.comphenix.protocol.ProtocolManager;
import me.adamix.mercury.core.MercuryCore;
import me.adamix.mercury.core.MercuryCorePlugin;
import me.adamix.mercury.core.player.MercuryPlayer;
import me.adamix.mercury.core.protocol.api.ProtocolHandler;
import me.adamix.mercury.core.protocol.api.data.EntityMetadata;
import me.adamix.mercury.core.protocol.v1_21_4.Handler_1_21_4;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class MercuryProtocol {
	private final @NotNull ProtocolManager protocolManager;
	private final @NotNull ProtocolHandler handler;

	public MercuryProtocol(@NotNull ProtocolManager protocolManager) {
		this.protocolManager = protocolManager;

		String version = Bukkit.getServer().getBukkitVersion().split("-")[0];
		this.handler = switch (version) {
			case "1.21.4" -> new Handler_1_21_4();
			// ToDo Add more versions

			default -> {
				// No protocol version can be found for bukkit version
				Boolean shutDown = MercuryCore.coreConfiguration().getBoolean("missing_protocol_shutdown");
				if (Boolean.TRUE.equals(shutDown)) {
					MercuryCore.stop("No protocol version has been found for " + version);
				}
				throw new IllegalStateException("Current version is not supported by protocol version: " + version + "!");
			}
		};
		MercuryCorePlugin.getCoreLogger().info("Protocol handler has been found: {}", handler.getClass().getSimpleName());
	}

	public @NotNull EntityMetadata createEntityMetadata(@NotNull Entity bukkitEntity) {
		return handler.createEntityMetadata(bukkitEntity);
	}

	public void sendEntityMetadata(@NotNull EntityMetadata metadata, @NotNull MercuryPlayer player) {
		Player bukkitPlayer = player.getBukkitPlayer();
		handler.sendEntityMetadata(metadata, bukkitPlayer, protocolManager);
	}
}
