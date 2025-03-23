package me.adamix.mercury.core.command;

import me.adamix.mercury.core.MercuryCore;
import me.adamix.mercury.core.mob.MercuryMob;
import me.adamix.mercury.core.mob.blueprint.MercuryMobBlueprint;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.Command;

public class MobCommand {
	@Command("mob spawn <key>")
	public void spawn(Player sender, MercuryMobBlueprint key) {
		MercuryMob mob = key.build();
		MercuryCore.spawnMob(mob, sender.getLocation());
	}
}
