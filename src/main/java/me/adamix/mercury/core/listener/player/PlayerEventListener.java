package me.adamix.mercury.core.listener.player;

import io.papermc.paper.event.packet.PlayerChunkLoadEvent;
import me.adamix.mercury.core.MercuryCore;
import me.adamix.mercury.core.attribute.AttributeUpdater;
import me.adamix.mercury.core.mob.MercuryMob;
import me.adamix.mercury.core.player.MercuryPlayer;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerEventListener implements Listener {

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		// Remove default join message
		event.joinMessage(Component.empty());

		MercuryPlayer player = MercuryCore.playerManager().createPlayer(event.getPlayer());
		// ToDO Call some custom player join event
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		// Remove default quit message
		event.quitMessage(Component.empty());

		MercuryCore.playerManager().removePlayer(event.getPlayer().getUniqueId());
		// ToDo Call some custom player quit event
		// ToDO Get mercury player instance of player that just left
	}

	@EventHandler
	public void onSlotChange(PlayerItemHeldEvent event) {
		Player bukkitPlayer = event.getPlayer();
		MercuryPlayer player = MercuryCore.getPlayer(bukkitPlayer.getUniqueId());

		AttributeUpdater.update(player, event.getNewSlot(), event.getPreviousSlot());
	}

	@EventHandler
	public void onPlayerChunkLoad(PlayerChunkLoadEvent event) {
		Player bukkitPlayer = event.getPlayer();
		MercuryPlayer player = MercuryCore.getPlayer(bukkitPlayer.getUniqueId());

		// Not sure if we still need this

//		for (@NotNull Entity entity : event.getChunk().getEntities()) {
//			MercuryMob mob = MercuryCore.mobManager().getMob(entity.getUniqueId());
//			if (mob != null) {
//				// Mob names cannot be updated instantly. Therefore we wait for 1 tick
//				MercuryCore.runDelayed(1, () -> {
//					mob.updateName(player);
//				});
//			}
//		}
	}
}
