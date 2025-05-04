package me.adamix.mercury.core.command;

import me.adamix.mercury.api.MercuryCore;
import me.adamix.mercury.api.item.MercuryItem;
import me.adamix.mercury.api.item.blueprint.MercuryItemBlueprint;
import me.adamix.mercury.api.player.MercuryPlayer;
import me.adamix.mercury.core.item.CoreMercuryItem;
import me.adamix.mercury.core.item.blueprint.CoreMercuryItemBlueprint;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.Command;

import java.util.UUID;

public class ItemCommand {
	@Command("item give <key>")
	public void give(Player sender, MercuryItemBlueprint key) {
		MercuryPlayer player = MercuryCore.getPlayer(sender.getUniqueId());

		MercuryItem item = key.build(UUID.randomUUID());
		player.getInventory().setItem(0, item);
		player.getInventory().update(player);
	}
}
