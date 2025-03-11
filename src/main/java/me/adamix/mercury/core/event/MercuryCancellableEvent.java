package me.adamix.mercury.core.event;

public interface MercuryCancellableEvent {
	void setCancelled(boolean value);
	boolean isCancelled();
}
