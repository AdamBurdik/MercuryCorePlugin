package me.adamix.mercury.core.event.mob;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.adamix.mercury.core.mob.MercuryMob;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

@Getter
@AllArgsConstructor
public class MobSpawnEvent implements MercuryMobEvent {
	private final @NotNull MercuryMob mob;
	private final @NotNull Location location;
}
