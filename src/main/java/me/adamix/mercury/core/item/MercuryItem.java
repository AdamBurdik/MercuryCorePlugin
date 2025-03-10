package me.adamix.mercury.core.item;

import me.adamix.mercury.core.MercuryCore;
import me.adamix.mercury.core.attribute.MercuryAttribute;
import me.adamix.mercury.core.attribute.MercuryAttributeValue;
import me.adamix.mercury.core.common.ColorPallet;
import me.adamix.mercury.core.item.component.ItemAttributeComponent;
import me.adamix.mercury.core.item.component.ItemDescriptionComponent;
import me.adamix.mercury.core.item.component.ItemRarityComponent;
import me.adamix.mercury.core.item.component.MercuryItemComponent;
import me.adamix.mercury.core.item.rarity.ItemRarity;
import me.adamix.mercury.core.player.MercuryPlayer;
import me.adamix.mercury.core.translation.Translation;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.attribute.AttributeModifier;
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

//	private static final Tag<UUID> uniqueIdTag = Tag.UUID("uniqueId");

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
//		PlaceholderManager placeholderManager = Server.getPlaceholderManager();
		Translation translation = MercuryCore.getPlayerTranslation(player);

		MiniMessage miniMessage = MiniMessage.miniMessage();

		// Create lore list
		List<Component> loreList = new ArrayList<>();

		// Add description to lore if available
		ItemDescriptionComponent itemDescriptionComponent = getComponent(ItemDescriptionComponent.class);
		if (itemDescriptionComponent != null) {
			for (String line : itemDescriptionComponent.lines()) {
//				Component lineComponent = placeholderManager.parse(line, player);
				loreList.add(miniMessage.deserialize(line));
			}
		}

		ItemAttributeComponent itemAttributeComponent = getComponent(ItemAttributeComponent.class);
		if (itemAttributeComponent != null) {
			// Add DPS to lore if available
			MercuryAttributeValue damage = itemAttributeComponent.get(MercuryAttribute.DAMAGE);
			MercuryAttributeValue attackSpeed = itemAttributeComponent.get(MercuryAttribute.ATTACK_SPEED);
			if (damage != null && attackSpeed != null) {
				loreList.add(Component.empty());
				loreList.add(
						miniMessage.deserialize(translation.get("item.dps") + ": ")
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
			itemAttributeComponent.attributeMap().forEach((attribute, value) -> {
				if (value == null) {
					return;
				}

				if (attribute == MercuryAttribute.DAMAGE || attribute == MercuryAttribute.ATTACK_SPEED) {
					return;
				}

				Component namePart = Component.text(translation.get(attribute.translationKey()) + ": ")
						.color(ColorPallet.LIGHT_GRAY.getColor());

				boolean isPositive = value.value() >= 0;
				String sign = isPositive ? "+" : "";

				Component valuePart = formatAttribute(value, sign, isPositive);

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
					miniMessage.deserialize(translation.get(rarity.translationKey()).toUpperCase())
							.color(TextColor.color(129, 21, 13))
							.decoration(TextDecoration.ITALIC, false)
							.decoration(TextDecoration.BOLD, true)
			);
		}

		// Add debug to lore if player is in debug
		if (player.isDebug()) {
			loreList.add(Component.empty());
			loreList.add(
					miniMessage.deserialize("uniqueId: " + this.uniqueId)
							.color(ColorPallet.DARK_GRAY.getColor())
			);
			loreList.add(
					miniMessage.deserialize("BlueprintID: " + this.blueprintKey.asString())
							.color(ColorPallet.DARK_GRAY.getColor())
			);
			List<String> componentNameArray = new ArrayList<>();
			for (@NotNull MercuryItemComponent component : this.components) {
				componentNameArray.add(component.getClass().getSimpleName());
			}

			loreList.add(
					miniMessage.deserialize("Components: " + componentNameArray)
							.color(ColorPallet.DARK_GRAY.getColor())
			);
		}

		ItemStack itemStack = new ItemStack(this.material);
		ItemMeta meta = itemStack.getItemMeta();

		meta.customName(miniMessage.deserialize(this.name));
		meta.lore(loreList);
//		meta.setHideTooltip(true);

		itemStack.setItemMeta(meta);

		return itemStack;
	}

	private @NotNull Component formatAttribute(MercuryAttributeValue value, String sign, boolean isPositive) {
		AttributeModifier.Operation operation = value.operation();
		Component valuePart = switch (operation) {
			case ADD_NUMBER -> Component.text(sign + (int) value.value());
			case ADD_SCALAR -> Component.text(sign + (int) (value.value() * 100) + "%");
			case MULTIPLY_SCALAR_1 -> Component.text((int) (value.value() * 100) + "%");
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
