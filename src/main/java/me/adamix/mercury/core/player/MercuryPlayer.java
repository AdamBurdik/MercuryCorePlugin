package me.adamix.mercury.core.player;

import lombok.Getter;
import lombok.NonNull;
import me.adamix.mercury.core.MercuryCore;
import me.adamix.mercury.core.exception.TranslationNotFoundException;
import me.adamix.mercury.core.translation.Translation;
import net.kyori.adventure.text.Component;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

@Getter
public class MercuryPlayer {
	private final @NotNull Player bukkitPlayer;
	private @NotNull String translationId;
	private boolean debug = true;

	public MercuryPlayer(
			@NotNull Player bukkitPlayer,
			@NotNull String translationId
	) {
		this.bukkitPlayer = bukkitPlayer;
		this.translationId = translationId;
	}

	/**
	 * Retrieves the name of player.
	 * @return the name of player
	 */
	public @NotNull String name() {
		return bukkitPlayer.getName();
	}

	/**
	 * Sets the translation of player.
	 * @param translationId new translation id.
	 * @throws TranslationNotFoundException if translation with specified id does not exist.
	 */
	public void setTranslation(@NonNull String translationId) {
		if (MercuryCore.translationManager().getTranslation(translationId) == null) {
			throw new TranslationNotFoundException(translationId);
		}
		this.translationId = translationId;
	}

	/**
	 * Get translation message from configuration and send it to player.
	 * @param dottedKey dotted key of translation.
	 */
	public void sendTranslatableMessage(@NonNull String dottedKey) {
		sendTranslatableMessage(dottedKey, Map.of());
	}

	/**
	 * Gets translation message from configuration, parse arguments and send it to player.
	 * @param dottedKey dotted key of translation.
	 * @param argumentMap argument map to parse.
	 */
	public void sendTranslatableMessage(@NonNull String dottedKey, @NonNull Map<String, Object> argumentMap) {
		@NotNull Translation translation = MercuryCore.getPlayerTranslation(this);
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
