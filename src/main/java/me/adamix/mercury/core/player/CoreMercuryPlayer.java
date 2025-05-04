package me.adamix.mercury.core.player;

import lombok.NonNull;
import me.adamix.mercury.api.MercuryCore;
import me.adamix.mercury.api.attribute.AttributeContainer;
import me.adamix.mercury.api.player.MercuryPlayer;
import me.adamix.mercury.api.player.inventory.MercuryPlayerInventory;
import me.adamix.mercury.api.translation.MercuryTranslation;
import me.adamix.mercury.core.MercuryCoreImpl;
import me.adamix.mercury.core.exception.TranslationNotFoundException;
import me.adamix.mercury.core.player.inventory.CoreMercuryPlayerInventory;
import net.kyori.adventure.text.Component;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class CoreMercuryPlayer implements MercuryPlayer {
	private final @NotNull Player bukkitPlayer;
	private final @NotNull MercuryPlayerInventory inventory;
	private @NotNull String translationId;
	private final @NotNull AttributeContainer attributeContainer;
	private boolean debug = true;

	public CoreMercuryPlayer(
			@NotNull Player bukkitPlayer,
			@NotNull String translationId,
			@NotNull AttributeContainer attributeContainer
	) {
		this.bukkitPlayer = bukkitPlayer;
		this.translationId = translationId;
		this.attributeContainer = attributeContainer;
		this.inventory = new CoreMercuryPlayerInventory(bukkitPlayer.getInventory());
	}

	public boolean inDebug() {
		return debug;
	}

	public @NotNull String getName() {
		return bukkitPlayer.getName();
	}

	@Override
	public @NotNull Player getBukkitPlayer() {
		return bukkitPlayer;
	}

	@Override
	public @NotNull String getTranslationId() {
		return translationId;
	}

	@Override
	public @NotNull MercuryPlayerInventory getInventory() {
		return inventory;
	}

	@Override
	public @NotNull AttributeContainer getAttributeContainer() {
		return attributeContainer;
	}

	@Override
	public void setTranslation(@NonNull String translationId) {
		if (MercuryCoreImpl.translationManager().getTranslation(translationId) == null) {
			throw new TranslationNotFoundException(translationId);
		}
		this.translationId = translationId;
	}

    @Override
	 public void sendTranslatableMessage(@NonNull String dottedKey, @NonNull Map<String, Object> argumentMap) {
		@NotNull MercuryTranslation translation = MercuryCore.getPlayerTranslation(this);
		String translationMessage = translation.get(dottedKey);

		if (!argumentMap.isEmpty()) {
			StringBuilder messageBuilder = new StringBuilder(translationMessage);
			argumentMap.forEach((name, value) -> {
				int start = messageBuilder.indexOf("<" + name + ">");
				if (start != -1) {
					int end = start + name.length() + 2;
					messageBuilder.replace(start, end, value.toString());
				}
			});
			translationMessage = messageBuilder.toString();
		}
		sendMessage(Component.text(translationMessage));
	}

	/**
	 * Sends a message to player
	 * @param component message component.
	 */
	public void sendMessage(@NotNull Component component) {
		bukkitPlayer.sendMessage(component);
	}

	public @Nullable AttributeInstance getBukkitAttribute(@NotNull Attribute bukkitAttribute) {
		return bukkitPlayer.getAttribute(bukkitAttribute);
	}
}
