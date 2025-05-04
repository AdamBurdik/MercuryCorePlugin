package me.adamix.mercury.core.data;

import me.adamix.mercury.api.data.DataHolder;
import me.adamix.mercury.api.data.DataInstance;
import me.adamix.mercury.api.data.DataManager;
import me.adamix.mercury.core.MercuryCorePlugin;
import org.dizitart.no2.collection.Document;
import org.dizitart.no2.collection.DocumentCursor;
import org.dizitart.no2.collection.NitriteCollection;
import org.dizitart.no2.collection.UpdateOptions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.dizitart.no2.filters.FluentFilter.where;

public class CoreDataManager implements DataManager {
	private final CoreDatabase coreDatabase;

	private final Map<Class<? extends DataInstance>, DataHolder<?>> holderMap = new HashMap<>();

	public CoreDataManager(@NotNull String filename) {
		this.coreDatabase = new CoreDatabase(filename);
	}

	@Override
	public <T extends DataInstance> void registerDataHolder(@NotNull DataHolder<T> dataHolder, @NotNull Class<? extends DataInstance> clazz) {
		holderMap.put(clazz, dataHolder);
		MercuryCorePlugin.getCoreLogger().info("Registered data holder {} for {}", dataHolder.getClass().getSimpleName(), clazz.getSimpleName());
	}

	@Override
	public <T extends DataInstance> @Nullable DataHolder<T> getDataHolder(@NotNull Class<T> clazz) {
		return (DataHolder<T>) holderMap.get(clazz);
	}

	@Override
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

	@Override
	public<T extends DataInstance> void unloadData(@NotNull UUID uuid, Class<T> clazz) {
		DataHolder<T> holder = getDataHolder(clazz);
		if (holder == null) {
			throw new IllegalStateException("No data holder registered for " + clazz.getName());
		}

		holder.unload(uuid);
	}

	@Override
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

	@Override
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

	@Override
	public void close() {
		coreDatabase.close();
	}
}
