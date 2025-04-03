package me.adamix.mercury.core.placeholder;

import me.adamix.mercury.core.MercuryCore;
import me.adamix.mercury.core.MercuryCorePlugin;
import me.adamix.mercury.core.attribute.MercuryAttribute;
import me.adamix.mercury.core.mob.MercuryMob;
import me.adamix.mercury.core.mob.component.MobAttributeComponent;
import me.adamix.mercury.core.player.MercuryPlayer;
import me.adamix.mercury.core.translation.Translation;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class PlaceholderManager {
	private final Map<String, Placeholder> placeholderMap = new HashMap<>();

	public PlaceholderManager() {
		registerPlaceholder("player", (player, args, data) -> {
			if (!args.hasNext()) {
				return player.name();
			}

			Tag.Argument arg = args.pop();
			return switch (arg.lowerValue()) {
				case "name" -> player.name();
				default -> throw new IllegalArgumentException("Unknown argument: " + arg.value());
			};
		});
		registerPlaceholder("translation", (player, args, data) -> {
			if (!args.hasNext()) {
				throw new IllegalArgumentException("No translation key was provided!");
			}

			Tag.Argument argument = args.pop();
			String key = argument.lowerValue();

			Translation translation = MercuryCore.getPlayerTranslation(player);
			return translation.get(key);
		});
		registerPlaceholder("mob", (player, args, data) -> {
			Object mobObject = data.get("mob");
			if (!(mobObject instanceof MercuryMob mob)) {
				throw new IllegalArgumentException("Expected MercuryMob under key 'mob', but none was provided.");
			}

			if (!args.hasNext()) {
				return mob.getName();
			}

			Tag.Argument arg = args.pop();

			return switch (arg.lowerValue()) {
				case "name" -> mob.getName();
				case "type" -> mob.getEntityType().key().asString();
				case "health" -> mob.getComponent(MobAttributeComponent.class).get(MercuryAttribute.HEALTH).toString();
				case "max_health" -> mob.getComponent(MobAttributeComponent.class).get(MercuryAttribute.MAX_HEALTH).toString();
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
//			Translation translation = MercuryCore.getPlayerTranslation(player);
//			return translation.get(key);
//		});
	}

	public void registerPlaceholder(@NotNull String name, @NotNull Placeholder placeholder) {
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

	@FunctionalInterface
	public interface Placeholder {
		String get(MercuryPlayer player, ArgumentQueue args, Map<String, Object> data);
	}
}
