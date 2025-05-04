package me.adamix.mercury.core.placeholder;

import me.adamix.mercury.api.MercuryCore;
import me.adamix.mercury.api.attribute.MercuryAttribute;
import me.adamix.mercury.api.entity.MercuryEntity;
import me.adamix.mercury.api.placeholder.MercuryPlaceholder;
import me.adamix.mercury.api.placeholder.PlaceholderManager;
import me.adamix.mercury.api.player.MercuryPlayer;
import me.adamix.mercury.api.translation.MercuryTranslation;
import me.adamix.mercury.core.MercuryCorePlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class CorePlaceholderManager implements PlaceholderManager {
	private final Map<String, MercuryPlaceholder> placeholderMap = new HashMap<>();

	public CorePlaceholderManager() {
		registerPlaceholder("player", (player, args, data) -> {
			if (!args.hasNext()) {
				return player.getName();
			}

			Tag.Argument arg = args.pop();
			return switch (arg.lowerValue()) {
				case "name" -> player.getName();
				default -> throw new IllegalArgumentException("Unknown argument: " + arg.value());
			};
		});
		registerPlaceholder("translation", (player, args, data) -> {
			if (!args.hasNext()) {
				throw new IllegalArgumentException("No translation key was provided!");
			}

			Tag.Argument argument = args.pop();
			String key = argument.lowerValue();

			MercuryTranslation translation = MercuryCore.getPlayerTranslation(player);
			return translation.get(key);
		});
		registerPlaceholder("entity", (player, args, data) -> {
			Object mobObject = data.get("mob");
			if (!(mobObject instanceof MercuryEntity entity)) {
				throw new IllegalArgumentException("Expected MercuryMob under key 'mob', but none was provided.");
			}

			if (!args.hasNext()) {
				return entity.toString();
			}

			Tag.Argument arg = args.pop();

			return switch (arg.lowerValue()) {
				case "type" -> entity.type().getName();
				case "health" -> String.valueOf(entity.getHealth());
				case "max_health" -> String.valueOf(entity.getMaxHealth());
				default -> throw new IllegalArgumentException("Unknown argument: " + arg.value());
			};
		});
//		registerPlaceholder("player_name", (args, player) -> player.name());
//		registerPlaceholder("translation", (args, player) -> {
//			if (!args.hasNext()) {
//				return "Invalid Key";
//			}
//
//		 	Tag.Argument argument = args.pop();
//			String key = argument.lowerValue();
//
//			CoreMercuryTranslation translation = MercuryCoreImpl.getPlayerTranslation(player);
//			return translation.get(key);
//		});
	}

	public void registerPlaceholder(@NotNull String name, @NotNull MercuryPlaceholder placeholder) {
		placeholderMap.put(name, placeholder);
	}

	private TagResolver getTagResolver(@NotNull MercuryPlayer player, @NotNull Map<String, Object> data) {
		TagResolver.Builder builder = TagResolver.builder();

		placeholderMap.forEach((name, placeholder) -> {
			builder.tag(name, (args, ctx) -> {
				try {
					return Tag.preProcessParsed(placeholder.get(player, args, data));
				} catch (RuntimeException e) {
					MercuryCorePlugin.getCoreLogger().error("Error while parsing placeholder {}: {}", name, e.getMessage());
				}
				return Tag.preProcessParsed(name + ":" + args.toString());
			});
		});

		return builder.build();
	}

	public Component parse(@NotNull String text, @NotNull MercuryPlayer player, @NotNull Map<String, Object> data) {
		return MiniMessage.miniMessage().deserialize(text, getTagResolver(player, data));
	}

	public Component parse(@NotNull String text, @NotNull MercuryPlayer player) {
		return MiniMessage.miniMessage().deserialize(text, getTagResolver(player, Map.of()));
	}
}
