package me.adamix.mercury.core.command;

import me.adamix.mercury.api.MercuryCore;
import me.adamix.mercury.api.entity.MercuryEntity;
import me.adamix.mercury.api.entity.blueprint.MercuryEntityBlueprint;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.Command;

public class EntityCommand {
	@Command("mob spawn <key>")
	public void spawn(Player sender, MercuryEntityBlueprint key) {
		MercuryEntity entity = key.build();
		MercuryCore.spawnEntity(entity, sender.getLocation());
	}
}
