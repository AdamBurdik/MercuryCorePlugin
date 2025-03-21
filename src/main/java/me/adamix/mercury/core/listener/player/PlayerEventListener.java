package me.adamix.mercury.core.listener.player;

import io.papermc.paper.event.player.PlayerInventorySlotChangeEvent;
import me.adamix.mercury.core.MercuryCore;
import me.adamix.mercury.core.attribute.AttributeUpdater;
import me.adamix.mercury.core.player.MercuryPlayer;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

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

		AttributeUpdater.update(player, event.getNewSlot());
	}
}
