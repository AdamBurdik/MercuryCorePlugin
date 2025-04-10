package me.adamix.mercury.core.event.command;

import lombok.Getter;
import lombok.Setter;
import me.adamix.mercury.core.event.MercuryCancellableEvent;
import me.adamix.mercury.core.event.MercuryEvent;
import org.bukkit.command.CommandSender;

@Getter
@Setter
public class ServerCommandEvent implements MercuryEvent, MercuryCancellableEvent {
	private final String command;
	private final CommandSender sender;
	private boolean cancelled;

	public ServerCommandEvent(String command, CommandSender sender) {
		this.command = command;
		this.sender = sender;
	}
}
