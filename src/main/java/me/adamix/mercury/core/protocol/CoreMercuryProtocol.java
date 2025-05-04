package me.adamix.mercury.core.protocol;

import com.comphenix.protocol.ProtocolManager;
import me.adamix.mercury.api.MercuryCore;
import me.adamix.mercury.api.entity.MercuryEntity;
import me.adamix.mercury.api.player.MercuryPlayer;
import me.adamix.mercury.api.protocol.MercuryProtocol;
import me.adamix.mercury.api.protocol.ProtocolHandler;
import me.adamix.mercury.api.protocol.data.EntityMetadata;
import me.adamix.mercury.core.MercuryCoreImpl;
import me.adamix.mercury.core.MercuryCorePlugin;
import me.adamix.mercury.core.protocol.v1_21_4.Handler_1_21_4;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CoreMercuryProtocol implements MercuryProtocol {
	private final @NotNull ProtocolManager protocolManager;
	private final @NotNull ProtocolHandler handler;

	public CoreMercuryProtocol(@NotNull ProtocolManager protocolManager) {
		this.protocolManager = protocolManager;

		String version = Bukkit.getServer().getBukkitVersion().split("-")[0];
		this.handler = switch (version) {
			case "1.21.4" -> new Handler_1_21_4();
			// ToDo Add more versions

			default -> {
				// No protocol version can be found for bukkit version
				// ToDO Reimplement this
//				Boolean shutDown = MercuryCore.coreConfiguration().getBoolean("missing_protocol_shutdown");
//				if (Boolean.TRUE.equals(shutDown)) {
//					// ToDo Add stop method to api interface, or figure out better way to handle stopping the plugin
//					((MercuryCoreImpl) MercuryCorePlugin.getInstance().getMercuryCore()).stop("No protocol version has been found for " + version);
//				}
				throw new IllegalStateException("Current version is not supported by protocol version: " + version + "!");
			}
		};
		MercuryCorePlugin.getCoreLogger().info("Protocol handler has been found: {}", handler.getClass().getSimpleName());
	}

	@Override
	public @NotNull EntityMetadata createEntityMetadata(@NotNull MercuryEntity entity) {
		return handler.createEntityMetadata(entity.bukkitEntity());
	}

	@Override
	public void sendEntityMetadata(@NotNull EntityMetadata metadata, @NotNull MercuryPlayer player) {
		Player bukkitPlayer = player.getBukkitPlayer();
		handler.sendEntityMetadata(metadata, bukkitPlayer, protocolManager);
	}
}
