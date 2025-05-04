package me.adamix.mercury.core.protocol.v1_21_4;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedDataValue;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.comphenix.protocol.wrappers.WrappedWatchableObject;
import me.adamix.mercury.api.protocol.ProtocolHandler;
import me.adamix.mercury.api.protocol.data.EntityMetadata;
import me.adamix.mercury.core.protocol.v1_21_4.data.EntityMetadata_1_21_4;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Handler_1_21_4 implements ProtocolHandler {
	@Override
	public @NotNull EntityMetadata createEntityMetadata(@NotNull Entity entity) {
		return new EntityMetadata_1_21_4(entity);
	}

	@Override
	public void sendEntityMetadata(@NotNull EntityMetadata metadata, @NotNull Player bukkitPlayer, @NotNull ProtocolManager protocolManager) {
		PacketContainer packet = new PacketContainer(PacketType.Play.Server.ENTITY_METADATA);
		if (!(metadata instanceof EntityMetadata_1_21_4 metadataHandler)) {
			throw new IllegalArgumentException("Expected EntityMetadata_1_21_4, got " + metadata.getClass().getSimpleName());
		}

		packet.getIntegers().write(0, metadataHandler.getBukkitEntity().getEntityId());
		WrappedDataWatcher watcher = metadataHandler.toWatcher();

		// Not really sure why this list is needed but it works.
		// https://www.spigotmc.org/threads/unable-to-modify-entity-metadata-packet-using-protocollib-1-19-3.582442/#post-4517187
		final List<WrappedDataValue> wrappedDataValueList = new ArrayList<>();

		for(final WrappedWatchableObject entry : watcher.getWatchableObjects()) {
			if(entry == null) continue;

			final WrappedDataWatcher.WrappedDataWatcherObject watcherObject = entry.getWatcherObject();
			wrappedDataValueList.add(
					new WrappedDataValue(
							watcherObject.getIndex(),
							watcherObject.getSerializer(),
							entry.getRawValue()
					)
			);
		}

		packet.getDataValueCollectionModifier().write(0, wrappedDataValueList);
		protocolManager.sendServerPacket(bukkitPlayer, packet);
	}
}
