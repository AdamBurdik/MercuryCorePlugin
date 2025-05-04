package me.adamix.mercury.core.protocol.v1_21_4.data;

import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import lombok.Getter;
import me.adamix.mercury.api.protocol.data.EntityMetadata;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class EntityMetadata_1_21_4 implements EntityMetadata {
	private final @NotNull Map<Integer, Object> metadataValues;
	@Getter
	private final @NotNull Entity bukkitEntity;

	public EntityMetadata_1_21_4(@NotNull Entity bukkitEntity) {
		this.metadataValues = new HashMap<>();
		this.bukkitEntity = bukkitEntity;
	}

	@Override
	public @NotNull EntityMetadata customName(@NotNull String name) {
		metadataValues.put(2, Optional.of(WrappedChatComponent.fromChatMessage(name)[0].getHandle()));
		return this;
	}

	@Override
	public @NotNull EntityMetadata customName(@NotNull Component name) {
		String json = GsonComponentSerializer.gson().serialize(name);
		WrappedChatComponent wrapped = WrappedChatComponent.fromJson(json);
		metadataValues.put(2, Optional.of(wrapped.getHandle()));
		return this;
	}

	@Override
	public @NotNull EntityMetadata customNameVisible(boolean value) {
		metadataValues.put(3, value);
		return this;
	}

	@Override
	public @NotNull EntityMetadata silent(boolean value) {
		metadataValues.put(4, value);
		return this;
	}

	@Override
	public @NotNull EntityMetadata gravity(boolean value) {
		metadataValues.put(5, value);
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
