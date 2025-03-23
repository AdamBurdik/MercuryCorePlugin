package me.adamix.mercury.core.toml.exception;

import org.jetbrains.annotations.NotNull;

public class MissingTomlPropertyException extends RuntimeException {
	public MissingTomlPropertyException(@NotNull String key, @NotNull String fileName) {
		super("Missing required property in " + fileName + ": " + key);
	}
}
