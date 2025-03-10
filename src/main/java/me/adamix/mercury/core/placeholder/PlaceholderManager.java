package me.adamix.mercury.core.placeholder;

import me.adamix.mercury.core.MercuryCore;
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
import java.util.UUID;
import java.util.function.BiFunction;

public class PlaceholderManager {
	private final Map<String, BiFunction<ArgumentQueue, MercuryPlayer, String>> playerPlaceholderMap = new HashMap<>();

	public PlaceholderManager() {
		registerPlayer("player_name", (args, player) -> player.name());
		registerPlayer("translation", (args, player) -> {
			if (!args.hasNext()) {
				return "Invalid Key";
			}

		 	Tag.Argument argument = args.pop();
			String key = argument.lowerValue();

			Translation translation = MercuryCore.getPlayerTranslation(player);
			return translation.get(key);
		});
	}

	public void registerPlayer(@NotNull String name, BiFunction<ArgumentQueue, MercuryPlayer, String> function) {
		playerPlaceholderMap.put(name, function);
	}

	private TagResolver getTagResolver(MercuryPlayer player) {
		TagResolver.Builder builder = TagResolver.builder();

		playerPlaceholderMap.forEach((name, function) -> {
			builder.tag(name, (args, context) -> Tag.preProcessParsed(function.apply(args, player)));
		});

		return builder.build();
	}

	public Component parse(String text, MercuryPlayer player) {
		return MiniMessage.miniMessage().deserialize(text, getTagResolver(player));
	}
}
