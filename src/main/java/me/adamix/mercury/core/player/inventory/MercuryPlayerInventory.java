package me.adamix.mercury.core.player.inventory;

import lombok.Getter;
import me.adamix.mercury.core.item.MercuryItem;
import me.adamix.mercury.core.player.MercuryPlayer;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MercuryPlayerInventory {
	private final @NotNull PlayerInventory bukkitInventory;
	@Getter
	private final MercuryItem[] items;

	public MercuryPlayerInventory(@NotNull PlayerInventory bukkitInventory, @NotNull MercuryItem[] items) {
		this.bukkitInventory = bukkitInventory;
		this.items = items;
	}

	public MercuryPlayerInventory(@NotNull PlayerInventory bukkitInventory) {
		this(bukkitInventory, new MercuryItem[41]);
	}

	public void setItem(int index, @Nullable MercuryItem item) {
		if (index < 0 || index >= items.length) {
			throw new IndexOutOfBoundsException("Index " + index + " is invalid for player inventory!");
		}
		items[index] = item;
	}
	
	public void addItem(@NotNull MercuryItem item) {
		for (int i = 0; i < items.length; i++) {
			if (items[i] == null) {
				items[i] = item;
				return;
			}
		}
	}

	public @Nullable MercuryItem get(int index) {
		if (index < 0 || index >= items.length) {
			throw new IndexOutOfBoundsException("Index " + index + " is invalid for player inventory!");
		}
		return items[index];
	}

	/**
	 * Move all items to bukkit inventory.
	 */
	public void update(@NotNull MercuryPlayer player) {
		int i = 0;
		for (MercuryItem item : items) {
			if (item != null) {
				// ToDo Add some cache to prevent converting items everytime.
				bukkitInventory.setItem(i, item.toItemStack(player));
			}

			i++;
		}
	}
}
