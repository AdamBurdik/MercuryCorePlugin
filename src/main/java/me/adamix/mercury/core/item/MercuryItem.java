package me.adamix.mercury.core.item;

import com.google.common.collect.HashMultimap;
import me.adamix.mercury.core.MercuryCore;
import me.adamix.mercury.core.attribute.MercuryAttribute;
import me.adamix.mercury.core.attribute.MercuryAttributeModifier;
import me.adamix.mercury.core.common.ColorPallet;
import me.adamix.mercury.core.item.component.ItemAttributeComponent;
import me.adamix.mercury.core.item.component.ItemDescriptionComponent;
import me.adamix.mercury.core.item.component.ItemRarityComponent;
import me.adamix.mercury.core.item.component.MercuryItemComponent;
import me.adamix.mercury.core.item.rarity.ItemRarity;
import me.adamix.mercury.core.placeholder.PlaceholderManager;
import me.adamix.mercury.core.player.MercuryPlayer;
import me.adamix.mercury.core.translation.Translation;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public record MercuryItem (
		@NotNull UUID uniqueId,
		@NotNull Key blueprintKey,
		@NotNull String name,
		@NotNull Material material,
		@NotNull MercuryItemComponent[] components
) {
	public MercuryItem(
			@NotNull UUID uniqueId,
			@NotNull Key blueprintKey,
			@NotNull String name,
			@NotNull Material material,
			@NotNull MercuryItemComponent[] components
	) {
		this.uniqueId = uniqueId;
		this.blueprintKey = blueprintKey;
		this.name = name;
		this.material = material;
		this.components = components;
	}

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

	public @NotNull ItemStack toItemStack(MercuryPlayer player) {
		PlaceholderManager placeholderManager = MercuryCore.placeholderManager();

		Translation translation = MercuryCore.getPlayerTranslation(player);

		// Create lore list
		List<Component> loreList = new ArrayList<>();

		// Add description to lore if available
		ItemDescriptionComponent itemDescriptionComponent = getComponent(ItemDescriptionComponent.class);
		if (itemDescriptionComponent != null) {
			for (String line : itemDescriptionComponent.lines()) {
				Component lineComponent = placeholderManager.parse(line, player);
				loreList.add(lineComponent);
			}
		}

		ItemAttributeComponent itemAttributeComponent = getComponent(ItemAttributeComponent.class);
		if (itemAttributeComponent != null) {
			// Add DPS to lore if available
			MercuryAttributeModifier damage = itemAttributeComponent.get(MercuryAttribute.DAMAGE);
			MercuryAttributeModifier attackSpeed = itemAttributeComponent.get(MercuryAttribute.ATTACK_SPEED);
			if (damage != null && attackSpeed != null) {
				loreList.add(Component.empty());
				loreList.add(
						Component.text(translation.get("item.dps") + ": ")
								.color(ColorPallet.GOLD.getColor())
								.decoration(TextDecoration.ITALIC, false)
								.append(Component.text(damage.value() * attackSpeed.value())
												.color(ColorPallet.GOLD.getColor())
												.decoration(TextDecoration.BOLD, true)
										)

				);
			}

			// Add attributes to lore if available
			loreList.add(Component.empty());
			itemAttributeComponent.attributeMap().forEach((attribute, modifier) -> {
//				if (attribute == MercuryAttribute.DAMAGE || attribute == MercuryAttribute.ATTACK_SPEED) {
//					return;
//				}

				Component namePart = Component.text(translation.get(attribute.translationKey()) + ": ")
						.color(ColorPallet.LIGHT_GRAY.getColor());

				boolean isPositive = modifier.value() >= 0;
				String sign = isPositive ? "+" : "";

				Component valuePart = formatAttribute(modifier, sign, isPositive);

				loreList.add(
						namePart.append(valuePart)
								.decoration(TextDecoration.ITALIC, false)
				);
			});
		}

		// Add rarity to lore if available
		ItemRarityComponent itemRarityComponent = getComponent(ItemRarityComponent.class);
		if (itemRarityComponent != null) {
			ItemRarity rarity = itemRarityComponent.rarity();
			loreList.add(Component.empty());
			loreList.add(
					Component.text(translation.get(rarity.translationKey()).toUpperCase())
							.color(TextColor.color(129, 21, 13))
							.decoration(TextDecoration.ITALIC, false)
							.decoration(TextDecoration.BOLD, true)
			);
		}

		// Add debug to lore if player is in debug
		if (player.isDebug()) {
			loreList.add(Component.empty());
			loreList.add(
					Component.text("uniqueId: " + this.uniqueId)
							.color(ColorPallet.DARK_GRAY.getColor())
			);
			loreList.add(
					Component.text("BlueprintID: " + this.blueprintKey.asString())
							.color(ColorPallet.DARK_GRAY.getColor())
			);
			List<String> componentNameArray = new ArrayList<>();
			for (@NotNull MercuryItemComponent component : this.components) {
				componentNameArray.add(component.getClass().getSimpleName());
			}

			loreList.add(
					Component.text("Components: " + componentNameArray)
							.color(ColorPallet.DARK_GRAY.getColor())
			);
		}

		ItemStack itemStack = new ItemStack(this.material);
		ItemMeta meta = itemStack.getItemMeta();
		meta.setAttributeModifiers(HashMultimap.create());

		meta.customName(placeholderManager.parse(this.name, player));
		meta.lore(loreList);

		itemStack.setItemMeta(meta);
		itemStack.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ADDITIONAL_TOOLTIP, ItemFlag.HIDE_DESTROYS);

		return itemStack;
	}

	private @NotNull Component formatAttribute(MercuryAttributeModifier modifier, String sign, boolean isPositive) {
		MercuryAttributeModifier.Operation operation = modifier.operation();
		Component valuePart = switch (operation) {
			case ADD_VALUE -> Component.text(sign + (int) modifier.value());
			case MULTIPLY_BASE -> Component.text(sign + (int) (modifier.value() * 100) + "%");
			case MULTIPLY_TOTAL -> Component.text((int) (modifier.value() * 100) + "%");
		};

		if (isPositive) {
			valuePart = valuePart.color(ColorPallet.POSITIVE_GREEN.getColor());
		} else {
			valuePart = valuePart.color(ColorPallet.NEGATIVE_RED.getColor());
		}
		return valuePart;
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) return true;
		if (object == null || getClass() != object.getClass()) return false;
		MercuryItem that = (MercuryItem) object;
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

	public @NotNull Map<String, Object> serialize() {
		Map<String, Object> map = new HashMap<>();

		map.put("uniqueId", this.uniqueId.toString());
		map.put("blueprintID", this.blueprintKey.toString());
		map.put("name", this.name);
		map.put("material", this.material.key().asString());

		Map<String, Object> componentMap = new HashMap<>();
		for (@NotNull MercuryItemComponent component : components) {
			componentMap.put(component.name(), component.serialize());
		}

		map.put("components", componentMap);
		return map;

	}

	@SuppressWarnings("unchecked")
	public static MercuryItem deserialize(Map<String, Object> map) {
		UUID uniqueId = UUID.fromString((String) map.get("uniqueId"));
		Key blueprintKey = Key.key((String) map.get("blueprintID"));
		String itemComponent = (String) map.get("name");
		Material material = Material.getMaterial((String) map.get("material"));
		Objects.requireNonNull(material);
		List<MercuryItemComponent> componentList = new ArrayList<>();
		Map<String, Object> componentMap = (Map<String, Object>) map.get("components");
		componentMap.forEach((name, value) -> {
			componentList.add(
					deserializeComponent(name, (Map<String, Object>) value)
			);
		});

		return new MercuryItem(
				uniqueId,
				blueprintKey,
				itemComponent,
				material,
				componentList.toArray(new MercuryItemComponent[0])
		);
	}

	public static @NotNull MercuryItemComponent deserializeComponent(String name, Map<String, Object> map) {
		return switch (name) {
			case "itemDescriptionComponent" -> ItemDescriptionComponent.deserialize(map);
			case "itemAttributeComponent" -> ItemAttributeComponent.deserialize(map);
			case "itemRarityComponent" -> ItemRarityComponent.deserialize(map);
			default -> throw new RuntimeException("No component found with name " + name);
		};

	}
}
