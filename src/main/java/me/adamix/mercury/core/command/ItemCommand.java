package me.adamix.mercury.core.command;

import me.adamix.mercury.core.MercuryCore;
import me.adamix.mercury.core.item.blueprint.MercuryItemBlueprint;
import me.adamix.mercury.core.player.MercuryPlayer;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.Command;

public class ItemCommand {
	@Command("item give <key>")
	public void give(Player sender, MercuryItemBlueprint key) {
		MercuryPlayer player = MercuryCore.getPlayer(sender.getUniqueId());

		// ToDO Add item to player inventory
	}
}
