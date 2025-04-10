package me.adamix.mercury.core.event;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public interface EventListener<E extends MercuryEvent> {
	public static <E extends MercuryEvent> Builder<E> builder(@NotNull Class<E> type) {
		return new Builder<>(type);
	}

	@NotNull Class<E> getEventType();

	void run(@NotNull E event);

	class Builder<E extends MercuryEvent> {
		private final Class<E> eventType;
		private final List<Predicate<E>> filters = new ArrayList<>();
		private Consumer<E> handler;

		public Builder(Class<E> eventType) {
			this.eventType = eventType;
		}

		public @NotNull Builder<E> handler(Consumer<E> handler) {
			this.handler = handler;
			return this;
		}

		public EventListener<E> build() {
			return new EventListener<E>() {
				@Override
				public @NotNull Class<E> getEventType() {
					return eventType;
				}

				@Override
				public void run(@NotNull E event) {
					if (!filters.isEmpty()) {
						for (Predicate<E> filter : filters) {
							if (filter.test(event)) {
								return;
							}
						}
					}
					if (handler != null) {
						handler.accept(event);
					}
				}
			};
		}
	}
}
