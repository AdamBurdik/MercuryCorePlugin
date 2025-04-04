package me.adamix.mercury.core.data;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class DataHolder<T extends DataInstance> {
	@Getter
	private final String holderId;
	private final @NotNull Map<UUID, T> dataMap = new HashMap<>();

	public DataHolder(@NotNull String holderId) {
		this.holderId = holderId;
	}

	/**
	 * Loads data from database to cache.
	 *
	 * @param uuid uuid of data to load.
	 */
	public void load(@NotNull UUID uuid, T dataInstance) {
		dataMap.put(uuid, dataInstance);
	}

	public void put(@NotNull UUID uuid, T data) {
		dataMap.put(uuid, data);
	}

	/**
	 * Unloads data from current cache. DOES NOT SAVE DATA TO DATABASE
	 *
	 * @param uuid uuid of data to unload.
	 */
	public void unload(@NotNull UUID uuid) {
		dataMap.remove(uuid);
	}

	/**
	 * Retrieves data from current cache.
	 *
	 * @param uuid uuid of data to retrieve.
	 * @return {@link T} instance, or null.
	 */
	public @Nullable T get(@NotNull UUID uuid) {
		return dataMap.get(uuid);
	}

	/**
	 * Creates instance of {@link T} populated with default data.
	 * @return {@link T} instance.
	 */
	public abstract @NotNull T createDefault();

	/**
	 * Creates instance of {@link T} with data from data map.
	 * @param data map with data.
	 * @return {@link T} instance.
	 */
	public abstract @NotNull T deserialize(@NotNull Map<String, Object> data);
}
