package me.adamix.mercury.core.event.mob;

import me.adamix.mercury.core.event.MercuryEvent;
import me.adamix.mercury.core.mob.MercuryMob;
import org.jetbrains.annotations.NotNull;

public interface MercuryMobEvent extends MercuryEvent {
	@NotNull MercuryMob getMob();
}
