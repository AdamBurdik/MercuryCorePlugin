package me.adamix.mercury.core.entity;

import me.adamix.mercury.api.entity.EntityManager;
import me.adamix.mercury.api.entity.MercuryEntity;
import me.adamix.mercury.api.math.MercuryPosition;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CoreEntityManager implements EntityManager {
	private final @NotNull Map<UUID, MercuryEntity> mobMap = new HashMap<>();

	@Override
	public @Nullable MercuryEntity getEntity(@NotNull UUID uuid) {
		return mobMap.get(uuid);
	}

	@Override
	public void spawn(@NotNull MercuryEntity entity, @NotNull World world, @NotNull MercuryPosition position) {
		//noinspection UnstableApiUsage
		entity.setWorld(world, position);

		UUID uuid = entity.getUuid();
		if (uuid != null) {
			mobMap.put(uuid, entity);
		}
	}

	@Override
	public List<MercuryEntity> getEntityList() {
		return mobMap.values().stream().toList();
	}
}
