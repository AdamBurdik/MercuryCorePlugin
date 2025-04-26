package me.adamix.mercury.core.mob.viewable;

import me.adamix.mercury.core.player.MercuryPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public interface Viewable {
	@NotNull Set<MercuryPlayer> getViewers();
	void addViewer(@NotNull MercuryPlayer player);
	void removeViewer(@NotNull MercuryPlayer player);
}
