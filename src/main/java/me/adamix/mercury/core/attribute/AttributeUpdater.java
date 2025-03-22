package me.adamix.mercury.core.attribute;

import me.adamix.mercury.core.item.MercuryItem;
import me.adamix.mercury.core.item.component.ItemAttributeComponent;
import me.adamix.mercury.core.player.MercuryPlayer;
import org.jetbrains.annotations.NotNull;

public class AttributeUpdater {
	public static void update(@NotNull MercuryPlayer player, int handSlot, int previousSlot) {
		MercuryItem item = player.getInventory().get(handSlot);
		AttributeContainer attributeContainer = player.getAttributeContainer();
		attributeContainer.clear();

		if (item != null) {
			if (item.hasComponent(ItemAttributeComponent.class)) {
				ItemAttributeComponent attributeComponent = item.getComponent(ItemAttributeComponent.class);
				attributeComponent.apply(attributeContainer);
			}
		}

		attributeContainer.apply(player);

		// ToDo Account for other item equipment
	}
}
