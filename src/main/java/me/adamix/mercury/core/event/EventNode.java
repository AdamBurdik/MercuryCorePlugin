package me.adamix.mercury.core.event;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.function.Predicate;

public class EventNode<E extends MercuryEvent> {
	private final @NotNull String name;
	private final @NotNull Class<E> eventType;
	private final @Nullable Predicate<E> filter;
	private final Map<Class<? extends E>, List<EventListener<E>>> listenerMap = new HashMap<>();
	private final Set<EventNode<E>> childSet = new CopyOnWriteArraySet<>();

	public static EventNode<MercuryEvent> all(@NotNull String name) {
		return new EventNode<>(name, MercuryEvent.class, null);
	}

	public EventNode(@NotNull String name, @NotNull Class<E> eventType, @Nullable Predicate<E> filter) {
		this.name = name;
		this.eventType = eventType;
		this.filter = filter;
	}

	public EventNode<E> addListener(@NotNull EventListener<? extends E> listener) {
		listenerMap.computeIfAbsent(listener.getEventType(), k -> new CopyOnWriteArrayList<>())
				.add((EventListener<E>) listener);
		return this;
	}

	public void call(@NotNull E event) {
		if (filter != null && filter.test(event)) {
			return;
		}
		List<EventListener<E>> listenerList = listenerMap.get(event.getClass());
		if (listenerList == null) {
			return;
		}
		for (EventListener<E> listener : listenerList) {
			listener.run(event);
		}
		for (EventNode<E> childNode : childSet) {
			childNode.call(event);
		}
	}
}
