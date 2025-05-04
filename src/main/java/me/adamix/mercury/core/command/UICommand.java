package me.adamix.mercury.core.command;

import me.adamix.mercury.api.MercuryCore;

import me.adamix.mercury.api.player.MercuryPlayer;
import me.adamix.mercury.core.ui.ScreenText;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class UICommand implements CommandExecutor {

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
		if (!(sender instanceof Player bukkitPlayer)) {
			return true;
		}

		MercuryPlayer player = MercuryCore.getPlayer(bukkitPlayer.getUniqueId());
		if (args[0].equals("text")) {
			System.out.println("yey");
			int lineOffset = Integer.parseInt(args[1]);
			System.out.println("offset: " + lineOffset);
			StringBuilder builder = new StringBuilder();
			for (int i = 2; i < args.length; i++) {
				builder.append(args[i]);
			}
			System.out.println("text: " + builder.toString());
			ScreenText.sendText(player, builder.toString(), lineOffset);
		}

		return false;
	}
}
