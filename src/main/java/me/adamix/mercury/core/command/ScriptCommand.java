package me.adamix.mercury.core.command;

import me.adamix.mercury.core.MercuryCoreImpl;
import me.adamix.mercury.core.MercuryCorePlugin;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.bukkit.actor.BukkitCommandActor;

public class ScriptCommand {
	@Command("script reload <script>")
	public void give(BukkitCommandActor sender, String script) {
		// ToDo Reimplement
//		MercuryCoreImpl.scriptManager().loadScript(MercuryCorePlugin.getFolderPath() + "/scripts/" + script);
//		sender.sendRawMessage("Reloaded " + script + "!");
	}
}
