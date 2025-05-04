package me.adamix.mercury.core.command;

import me.adamix.mercury.core.MercuryCoreImpl;
import me.adamix.mercury.core.data.DummyData;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.bukkit.actor.BukkitCommandActor;

import java.util.UUID;

public class TestCommand {
	@Command("test")
	public void test(BukkitCommandActor actor) {
		UUID uuid = UUID.fromString("aa37f90d-ab84-43b5-aca2-a94c782e69ca");

		MercuryCoreImpl.loadData(uuid, DummyData.class);
		var data = MercuryCoreImpl.getData(uuid, DummyData.class);
		actor.sendRawMessage("Data: " + data.toString());
		data.setScore(data.getScore() + 1);
		MercuryCoreImpl.saveData(uuid, DummyData.class);
	}
}
