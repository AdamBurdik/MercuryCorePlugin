package me.adamix.mercury.core.item;

import me.adamix.mercury.api.attribute.AttributeContainer;
import me.adamix.mercury.api.item.MercuryItem;
import me.adamix.mercury.api.item.MercuryItemBuilder;
import me.adamix.mercury.api.item.component.MercuryItemComponent;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CoreItemBuilder implements MercuryItemBuilder {
	private UUID uuid;
	private String name;
	private Material material;
	private AttributeContainer attributeContainer;
	private final List<MercuryItemComponent> componentList = new ArrayList<>();

	@Override
	public @NotNull MercuryItemBuilder uuid(@NotNull UUID uuid) {
		this.uuid = uuid;
		return this;
	}

	@Override
	public @NotNull MercuryItemBuilder name(@NotNull String name) {
		this.name = name;
		return this;
	}

	@Override
	public @NotNull MercuryItemBuilder material(@NotNull Material material) {
		this.material = material;
		return this;
	}

	@Override
	public @NotNull MercuryItemBuilder attributes(@NotNull AttributeContainer attributeContainer) {
		this.attributeContainer = attributeContainer;
		return this;
	}

	@Override
	public @NotNull MercuryItemBuilder components(@NotNull MercuryItemComponent... components) {
		componentList.addAll(List.of(components));
		return this;
	}

	@Override
	public @NotNull MercuryItem build() {
		if (uuid == null) {
			throw new IllegalStateException("Missing required field: uuid");
		}
		if (material == null) {
			throw new IllegalStateException("Missing required field: material");
		}
		return new CoreMercuryItem(
				uuid,
				null,
				name,
				material,
				attributeContainer,
				componentList.toArray(new MercuryItemComponent[0])
		);
	}
}
