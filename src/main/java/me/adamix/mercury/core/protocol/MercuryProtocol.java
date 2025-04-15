package me.adamix.mercury.core.protocol;

import com.comphenix.protocol.ProtocolManager;
import me.adamix.mercury.core.player.MercuryPlayer;
import me.adamix.mercury.core.protocol.api.ProtocolHandler;
import me.adamix.mercury.core.protocol.api.data.EntityMetadata;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class MercuryProtocol {
	private final @NotNull ProtocolManager protocolManager;
	private final @NotNull ProtocolHandler handler;

	public MercuryProtocol(@NotNull ProtocolManager protocolManager, @NotNull ProtocolHandler handler) {
		this.protocolManager = protocolManager;
		this.handler = handler;
	}

	public @NotNull EntityMetadata createEntityMetadata(@NotNull Entity bukkitEntity) {
		return handler.createEntityMetadata(bukkitEntity);
	}

	public void sendEntityMetadata(@NotNull EntityMetadata metadata, @NotNull MercuryPlayer player) {
		Player bukkitPlayer = player.getBukkitPlayer();
		handler.sendEntityMetadata(metadata, bukkitPlayer, protocolManager);
	}
}
