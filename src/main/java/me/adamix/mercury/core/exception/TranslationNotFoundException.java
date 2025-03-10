package me.adamix.mercury.core.exception;

import lombok.NonNull;

public class TranslationNotFoundException extends RuntimeException {
	public TranslationNotFoundException(@NonNull String translationId) {
		super("Unable to found translation with id " + translationId + "!");
	}
}
