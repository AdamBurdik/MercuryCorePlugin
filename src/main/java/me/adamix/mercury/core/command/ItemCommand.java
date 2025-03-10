package me.adamix.mercury.core.command;

import com.marcusslover.plus.lib.command.Command;
import com.marcusslover.plus.lib.command.CommandContext;
import com.marcusslover.plus.lib.command.ICommand;
import com.marcusslover.plus.lib.command.TabCompleteContext;
import me.adamix.mercury.core.MercuryCore;
import me.adamix.mercury.core.item.MercuryItem;
import me.adamix.mercury.core.player.MercuryPlayer;
import net.kyori.adventure.key.Key;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

@Command(name="item")
public class ItemCommand implements ICommand {
	@Override
	public boolean execute(@NotNull CommandContext ctx) {
		ctx.asPlayer(bukkitPlayer -> {
			MercuryPlayer player = new MercuryPlayer(bukkitPlayer, "en");

			Optional<MercuryItem> item = MercuryCore.itemManager().buildItem(Key.key("mercury", ctx.args()[0]));
			MercuryItem mercuryItem =  item.get();
			ItemStack itemStack = mercuryItem.toItemStack(player);
			bukkitPlayer.getInventory().addItem(itemStack);
		});
		return false;
	}
}
