package me.adamix.mercury.core.listener.command;

import me.adamix.mercury.core.MercuryCore;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerCommandEvent;

public class CommandListener implements Listener {
	@EventHandler
	public void onServerCommand(ServerCommandEvent event) {
		var mercuryEvent = new me.adamix.mercury.core.event.command.ServerCommandEvent(event.getCommand(), event.getSender());
		MercuryCore.getGlobalEventNode().call(mercuryEvent);
		event.setCancelled(mercuryEvent.isCancelled());
	}
}
