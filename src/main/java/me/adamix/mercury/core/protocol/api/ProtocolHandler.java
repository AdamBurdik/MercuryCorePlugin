package me.adamix.mercury.core.protocol.api;

import com.comphenix.protocol.ProtocolManager;
import me.adamix.mercury.core.protocol.api.data.EntityMetadata;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface ProtocolHandler {
	@NotNull EntityMetadata createEntityMetadata(@NotNull Entity bukkitEntity);
	void sendEntityMetadata(@NotNull EntityMetadata metadata, @NotNull Player bukkitPlayer, @NotNull ProtocolManager protocolManager);
}
