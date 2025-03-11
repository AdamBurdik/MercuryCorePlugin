package me.adamix.mercury.core.event.mob;

import lombok.Getter;
import lombok.Setter;
import me.adamix.mercury.core.event.MercuryCancellableEvent;
import me.adamix.mercury.core.mob.MercuryMob;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
public class MobFireTickEvent implements MercuryMobEvent, MercuryCancellableEvent {
	private final @NotNull MercuryMob mob;
	private boolean cancelled = false;

	public MobFireTickEvent(@NotNull MercuryMob mob) {
		this.mob = mob;
	}
}
