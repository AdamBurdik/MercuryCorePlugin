package me.adamix.mercury.core.command.types;

import me.adamix.mercury.core.MercuryCore;
import me.adamix.mercury.core.item.blueprint.MercuryItemBlueprint;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import revxrsal.commands.autocomplete.SuggestionProvider;
import revxrsal.commands.bukkit.actor.BukkitCommandActor;
import revxrsal.commands.exception.CommandErrorException;
import revxrsal.commands.node.ExecutionContext;
import revxrsal.commands.parameter.ParameterType;
import revxrsal.commands.stream.MutableStringStream;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ItemBlueprintParameterType implements ParameterType<BukkitCommandActor, MercuryItemBlueprint> {
	@Override
	public MercuryItemBlueprint parse(@NotNull MutableStringStream input, @NotNull ExecutionContext<@NotNull BukkitCommandActor> ctx) {
		String blueprintString = input.readString();
		Key blueprintKey = Key.key("mercury", blueprintString);
		Optional<MercuryItemBlueprint> blueprint = MercuryCore.itemBlueprintManager().get(blueprintKey);
		if (blueprint.isEmpty()) {
			throw new CommandErrorException("No such item: " + blueprintString);
		}

		return blueprint.get();
	}

	@Override
	public @NotNull SuggestionProvider<@NotNull BukkitCommandActor> defaultSuggestions() {
		return (ctx) -> {
			String arg = ctx.input().toMutableCopy().readString();

			return List.copyOf(
					MercuryCore.itemBlueprintManager().getItemIdCollection().stream()
							.map(Key::value)
							.filter(string -> string.startsWith(arg))
							.collect(Collectors.toSet())
			);
		};
	}
}
