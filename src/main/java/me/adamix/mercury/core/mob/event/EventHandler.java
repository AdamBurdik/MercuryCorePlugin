package me.adamix.mercury.core.mob.event;

import me.adamix.mercury.core.event.mob.MobFireTickEvent;
import me.adamix.mercury.core.event.mob.MobSpawnEvent;
import org.jetbrains.annotations.NotNull;

public interface EventHandler {
	default void onInit() {}
	default void onSpawn(@NotNull MobSpawnEvent event) {}
	default void onFireTick(@NotNull MobFireTickEvent event) {}
}
