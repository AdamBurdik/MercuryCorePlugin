package me.adamix.mercury.core.command.types;


import me.adamix.mercury.api.MercuryCore;
import me.adamix.mercury.api.entity.blueprint.MercuryEntityBlueprint;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import revxrsal.commands.autocomplete.SuggestionProvider;
import revxrsal.commands.bukkit.actor.BukkitCommandActor;
import revxrsal.commands.exception.CommandErrorException;
import revxrsal.commands.node.ExecutionContext;
import revxrsal.commands.parameter.ParameterType;
import revxrsal.commands.stream.MutableStringStream;

import java.util.List;
import java.util.stream.Collectors;

public class EntityBlueprintParameterType implements ParameterType<BukkitCommandActor, MercuryEntityBlueprint> {
	@Override
	public MercuryEntityBlueprint parse(@NotNull MutableStringStream input, @NotNull ExecutionContext<@NotNull BukkitCommandActor> ctx) {
		String blueprintString = input.readString();
		Key blueprintKey = Key.key("mercury", blueprintString);
		MercuryEntityBlueprint blueprint = MercuryCore.entityBlueprintManager().getBlueprint(blueprintKey);
		if (blueprint == null) {
			throw new CommandErrorException("No such mob: " + blueprintString);
		}

		return blueprint;
	}

	@Override
	public @NotNull SuggestionProvider<@NotNull BukkitCommandActor> defaultSuggestions() {
		return (ctx) -> {
			String arg = ctx.input().toMutableCopy().readString();

			return List.copyOf(
					MercuryCore.entityBlueprintManager().getKeySet().stream()
							.map(Key::value)
							.filter(string -> string.startsWith(arg))
							.collect(Collectors.toSet())
			);
		};
	}
}