package me.adamix.mercury.core.data;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

public interface DataInstance {
	@NotNull Map<String, Object> serialize();
}
