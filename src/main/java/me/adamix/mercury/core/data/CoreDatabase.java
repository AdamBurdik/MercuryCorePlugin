package me.adamix.mercury.core.data;

import org.dizitart.no2.Nitrite;
import org.dizitart.no2.collection.Document;
import org.dizitart.no2.collection.NitriteCollection;
import org.dizitart.no2.mvstore.MVStoreModule;
import org.jetbrains.annotations.NotNull;

public class CoreDatabase {
	private final Nitrite nitriteDb;

	public CoreDatabase(@NotNull String filename) {
		MVStoreModule storeModule = MVStoreModule.withConfig()
				.filePath(filename)
				.build();

		nitriteDb = Nitrite.builder()
				.loadModule(storeModule)
				.openOrCreate();
	}

	public NitriteCollection getCollection(@NotNull String id) {
		return nitriteDb.getCollection(id);
	}

	public void close() {
		nitriteDb.close();
	}
}
