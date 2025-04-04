package me.adamix.mercury.core.data;

import me.adamix.mercury.core.MercuryCorePlugin;
import org.dizitart.no2.collection.Document;
import org.dizitart.no2.collection.DocumentCursor;
import org.dizitart.no2.collection.NitriteCollection;
import org.dizitart.no2.collection.UpdateOptions;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.dizitart.no2.filters.FluentFilter.where;

public class DataManager {
	private final CoreDatabase coreDatabase;

	private final Map<Class<? extends DataInstance>, DataHolder<?>> holderMap = new HashMap<>();

	public DataManager(@NotNull String filename) {
		this.coreDatabase = new CoreDatabase(filename);
	}

	/**
	 * Registers data holder for specific data type.
	 * @param dataHolder holder instance to register.
	 * @param clazz the class of data to register holder for.
	 * @param <T> the type of data. Must implement {@link DataInstance}.
	 */
	public <T extends DataInstance> void registerDataHolder(@NotNull DataHolder<T> dataHolder, @NotNull Class<? extends DataInstance> clazz) {
		holderMap.put(clazz, dataHolder);
		MercuryCorePlugin.getCoreLogger().info("Registered data holder {} for {}", dataHolder.getClass().getSimpleName(), clazz.getSimpleName());
	}

	/**
	 * Retrieves data holder.
	 * @param clazz the class of data to retrieve holder for.
	 * @return {@link DataHolder <T>} instance
	 * @param <T> the type of data. Must implement {@link DataInstance}.
	 */
	public <T extends DataInstance> @Nullable DataHolder<T> getDataHolder(@NotNull Class<T> clazz) {
		return (DataHolder<T>) holderMap.get(clazz);
	}

	/**
	 * Loads data from database into cache.
	 * @param uuid unique id of data.
	 * @param clazz the class of data to load.
	 * @param <T> the type of data. Must implement {@link DataInstance}.
	 */
	public <T extends DataInstance> void loadData(@NotNull UUID uuid, Class<T> clazz) {
		DataHolder<T> holder = getDataHolder(clazz);
		if (holder == null) {
			throw new IllegalStateException("No data holder registered for " + clazz.getName());
		}

		NitriteCollection collection = coreDatabase.getCollection(holder.getHolderId());

		DocumentCursor cursor = collection.find(where("uuid").eq(uuid.toString()));
		Document document = cursor.firstOrNull();
		T instance;
		if (document == null) {
			instance = holder.createDefault();
			holder.load(uuid, instance);
			return;
		} else {
			Map<String, Object> map = new HashMap<>();
			for (String field : document.getFields()) {
				map.put(field, document.get(field));
			}

			instance = holder.deserialize(map);
		}
		holder.load(uuid, instance);
	}

	/**
	 * Unloads data from cache. DOES NOT SAVE TO DATABASE
	 * @param uuid unique id of data.
	 * @param <T> the type of data. Must implement {@link DataInstance}.
	 */
	public<T extends DataInstance> void unloadData(@NotNull UUID uuid, Class<T> clazz) {
		DataHolder<T> holder = getDataHolder(clazz);
		if (holder == null) {
			throw new IllegalStateException("No data holder registered for " + clazz.getName());
		}

		holder.unload(uuid);
	}

	/**
	 * Retrieveds data from cache.
	 * @param uuid unique id of data.
	 * @param clazz the class of data to retrieve.
	 * @return {@link T} instance.
	 * @param <T> the type of data. Must implement {@link DataInstance}.
	 */
	public <T extends DataInstance> @NotNull T getData(@NotNull UUID uuid, Class<T> clazz) {
		DataHolder<T> holder = getDataHolder(clazz);
		if (holder == null) {
			throw new IllegalStateException("No data holder registered for " + clazz.getName());
		}
		T data = holder.get(uuid);
		if (data == null) {
			data = holder.createDefault();
		}
		return data;
	}


	/**
	 * Saves data from cache to database. DOES NOT UNLOAD FROM CACHE
	 * @param uuid unique id of data.
	 * @param clazz the class of data to save.
	 * @param <T> the type of data. Must implement {@link DataInstance}
	 */
	public <T extends DataInstance> void saveData(@NotNull UUID uuid, Class<T> clazz) {
		DataHolder<T> holder = getDataHolder(clazz);
		if (holder == null) {
			throw new IllegalStateException("No data holder registered for " + clazz.getName());
		}

		T data = holder.get(uuid);
		if (data == null) {
			// ToDo Raise some error? idk
			return;
		}

		Map<String, Object> serialized = new HashMap<>(data.serialize());
		serialized.put("uuid", uuid.toString());
		Document document = Document.createDocument(serialized);
		String id = holder.getHolderId();
		NitriteCollection collection = coreDatabase.getCollection(id);
		collection.update(where("uuid").eq(uuid.toString()), document, UpdateOptions.updateOptions(true, true));
	}

	/**
	 * Closes database connection.
	 */
	@ApiStatus.Internal
	public void close() {
		coreDatabase.close();
	}
}
