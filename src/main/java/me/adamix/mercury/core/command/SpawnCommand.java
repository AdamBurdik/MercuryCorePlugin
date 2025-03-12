package me.adamix.mercury.core.command;

import com.marcusslover.plus.lib.command.Command;
import com.marcusslover.plus.lib.command.CommandContext;
import com.marcusslover.plus.lib.command.ICommand;
import com.marcusslover.plus.lib.command.TabCompleteContext;
import me.adamix.mercury.core.MercuryCore;
import me.adamix.mercury.core.mob.blueprint.MercuryMobBlueprint;
import me.adamix.mercury.core.player.MercuryPlayer;
import net.kyori.adventure.key.Key;
import org.apache.commons.lang.NotImplementedException;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

@Command(name="spawn")
public class SpawnCommand implements ICommand {
	@Override
	public boolean execute(@NotNull CommandContext ctx) {
		//      /spawn <key> <location> <count>

		ctx.asPlayer(bukkitPlayer -> {
			MercuryPlayer player = MercuryCore.getPlayer(bukkitPlayer.getUniqueId());

			@NotNull String[] args = ctx.args();
			if (args.length < 1) {
				player.sendTranslatableMessage("command.error.missing_argument", Map.of(
						"correct_usage", "/spawn <mob-id>"
				));
				return;
			}
			String stringMobId = args[0];
			Key blueprintId = Key.key(stringMobId);

			MercuryMobBlueprint mobBlueprint = MercuryCore.mobManager().getBlueprint(blueprintId);
			MercuryCore.mobManager().spawn(mobBlueprint.build(), bukkitPlayer.getLocation());
		});

		return false;
	}

	@Override
	public @NotNull List<@NotNull String> tab(@NotNull TabCompleteContext ctx) {
		return ICommand.super.tab(ctx);
	}
}
