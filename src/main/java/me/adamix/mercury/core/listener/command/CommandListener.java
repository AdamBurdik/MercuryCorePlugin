package me.adamix.mercury.core.listener.command;

import me.adamix.mercury.core.MercuryCore;
import me.adamix.mercury.core.script.data.LuaArgument;
import me.adamix.mercury.core.script.data.event.LuaCommandEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerCommandEvent;
import org.luaj.vm2.LuaTable;

public class CommandListener implements Listener {
	@EventHandler
	public void onServerCommand(ServerCommandEvent event) {
		var mercuryEvent = new me.adamix.mercury.core.event.command.ServerCommandEvent(event.getCommand(), event.getSender());
		MercuryCore.getGlobalEventNode().call(mercuryEvent);
		event.setCancelled(mercuryEvent.isCancelled());

		MercuryCore.scriptManager().runFunction("item/example_item.lua", "command", "on_command", new LuaArgument[]{
				new LuaCommandEvent(event)
		});
	}
}
