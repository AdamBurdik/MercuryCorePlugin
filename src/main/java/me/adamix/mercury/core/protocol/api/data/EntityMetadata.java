package me.adamix.mercury.core.protocol.api.data;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

public interface EntityMetadata {
	@NotNull EntityMetadata customName(@NotNull String name);
	@NotNull EntityMetadata customName(@NotNull Component name);
	@NotNull EntityMetadata customNameVisible(boolean value);
	@NotNull EntityMetadata silent(boolean value);
	@NotNull EntityMetadata gravity(boolean value);
}
