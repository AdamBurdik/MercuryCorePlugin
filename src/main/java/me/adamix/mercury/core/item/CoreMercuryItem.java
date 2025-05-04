package me.adamix.mercury.core.item;

import com.google.common.collect.HashMultimap;
import me.adamix.mercury.api.MercuryCore;
import me.adamix.mercury.api.attribute.AttributeContainer;
import me.adamix.mercury.api.item.MercuryItem;
import me.adamix.mercury.api.item.component.MercuryItemComponent;
import me.adamix.mercury.api.placeholder.PlaceholderManager;
import me.adamix.mercury.api.player.MercuryPlayer;
import net.kyori.adventure.key.Key;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public record CoreMercuryItem(
		@NotNull UUID uniqueId,
		@Nullable Key blueprintKey,
		@NotNull String name,
		@NotNull Material material,
		@NotNull AttributeContainer attributeContainer,
		@NotNull MercuryItemComponent[] components
) implements MercuryItem {

	public boolean hasComponent(Class<? extends MercuryItemComponent> clazz) {
		return getComponent(clazz) != null;
	}

	public <T extends MercuryItemComponent> @Nullable T getComponent(Class<T> clazz) {
		for (@NotNull MercuryItemComponent itemComponent : components) {
			if (itemComponent.getClass().equals(clazz)) {
				if (clazz.isInstance(itemComponent)) {
					return clazz.cast(itemComponent);
				}
			}
		}
		return null;
	}

	public @NotNull ItemStack toItemStack(@NotNull MercuryPlayer player) {
		//noinspection UnstableApiUsage
		PlaceholderManager placeholderManager = MercuryCore.placeholderManager();

		ItemStack itemStack = new ItemStack(this.material);
		ItemMeta meta = itemStack.getItemMeta();
		meta.setAttributeModifiers(HashMultimap.create());

		meta.customName(placeholderManager.parse(this.name, player));

		itemStack.setItemMeta(meta);
		itemStack.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ADDITIONAL_TOOLTIP, ItemFlag.HIDE_DESTROYS);

		return itemStack;
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) return true;
		if (object == null || getClass() != object.getClass()) return false;
		CoreMercuryItem that = (CoreMercuryItem) object;
		return Objects.equals(name, that.name)
				&& Objects.equals(uniqueId, that.uniqueId)
				&& Objects.equals(material, that.material)
				&& Objects.equals(blueprintKey, that.blueprintKey)
				&& Objects.deepEquals(components, that.components);
	}

	@Override
	public int hashCode() {
		return Objects.hash(uniqueId, blueprintKey, name, material, Arrays.hashCode(components));
	}
}
