package me.adamix.mercury.core.protocol.data;

import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class EntityMetadata {
	private final @NotNull Map<Integer, Object> metadataValues;
	@Getter
	private final @NotNull Entity entity;

	public EntityMetadata(@NotNull Entity entity) {
		this.metadataValues = new HashMap<>();
		this.entity = entity;
	}

	public @NotNull EntityMetadata setCustomName(@NotNull String name) {
		metadataValues.put(2, Optional.of(WrappedChatComponent.fromChatMessage(name)[0].getHandle()));
		return this;
	}

	public @NotNull EntityMetadata setCustomName(@NotNull Component name) {
		String json = GsonComponentSerializer.gson().serialize(name);
		WrappedChatComponent wrapped = WrappedChatComponent.fromJson(json);
		metadataValues.put(2, Optional.of(wrapped.getHandle()));
		return this;
	}

	public @NotNull EntityMetadata setCustomNameVisible(boolean visible) {
		metadataValues.put(3, visible);
		return this;
	}

	public @NotNull EntityMetadata setSilent(boolean isSilent) {
		metadataValues.put(4, isSilent);
		return this;
	}

	public @NotNull EntityMetadata setNoGravity(boolean noGravity) {
		metadataValues.put(5, noGravity);
		return this;
	}

	public @NotNull WrappedDataWatcher toWatcher() {
		WrappedDataWatcher watcher = new WrappedDataWatcher();

		for (Map.Entry<Integer, Object> entry : metadataValues.entrySet()) {
			int index = entry.getKey();
			Object value = entry.getValue();
			WrappedDataWatcher.Serializer serializer = getSerializer(value);

			if (serializer != null) {
				watcher.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(index, serializer), value);
			}
		}

		return watcher;
	}

	private WrappedDataWatcher.Serializer getSerializer(Object value) {
		if (value instanceof Boolean) return WrappedDataWatcher.Registry.get(Boolean.class);
		if (value instanceof Integer) return WrappedDataWatcher.Registry.get(Integer.class);
		if (value instanceof Float) return WrappedDataWatcher.Registry.get(Float.class);
		if (value instanceof Optional<?>) return WrappedDataWatcher.Registry.getChatComponentSerializer(true);
		return null;
	}
}
