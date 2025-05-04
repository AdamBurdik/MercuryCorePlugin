package me.adamix.mercury.core.entity.blueprint;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import me.adamix.mercury.api.attribute.AttributeContainer;
import me.adamix.mercury.api.entity.MercuryEntity;
import me.adamix.mercury.api.entity.blueprint.EntityBlueprintManager;
import me.adamix.mercury.api.entity.blueprint.MercuryEntityBlueprint;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CoreEntityBlueprintManager implements EntityBlueprintManager {
	private final Map<Key, MercuryEntityBlueprint> blueprintMap = new HashMap<>();

	@Override
	public void registerBlueprint(@NotNull MercuryEntityBlueprint blueprint) {
		blueprintMap.put(blueprint.key(), blueprint);
	}

	@Override
	public void registerAllItemBlueprints(@NotNull Path path) {
		for (MercuryEntityBlueprint mercuryEntityBlueprint : EntityBlueprintParser.parseAll(path)) {
			registerBlueprint(mercuryEntityBlueprint);
		}
	}

	@Override
	public void unloadBlueprints() {
		blueprintMap.clear();
	}

	@Override
	public @Nullable MercuryEntityBlueprint getBlueprint(@NotNull Key key) {
		return blueprintMap.get(key);
	}

	@Override
	public @NotNull Set<Key> getKeySet() {
		return blueprintMap.keySet();
	}
}
