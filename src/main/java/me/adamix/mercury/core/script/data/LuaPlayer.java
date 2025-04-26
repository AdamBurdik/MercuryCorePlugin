package me.adamix.mercury.core.script.data;

import me.adamix.mercury.core.MercuryCore;
import me.adamix.mercury.core.player.MercuryPlayer;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.VarArgFunction;

import static me.adamix.mercury.core.script.data.LuaData.bind;

public class LuaPlayer implements LuaArgument {
	private final LuaTable meta;

	public LuaPlayer(@NotNull MercuryPlayer player) {
		this.meta = new LuaTable();
		bindAll(player);
	}

	public void bindAll(@NotNull MercuryPlayer player) {
		bind(meta, "send_message", (arg) -> {
			String message = arg.tojstring();
			Component component = MercuryCore.placeholderManager().parse(message, player);
			player.sendMessage(component);
			return LuaValue.NIL;
		});
		bind(meta, "send_translatable_message", new VarArgFunction() {
			@Override
			public Varargs invoke(Varargs args) {
				if (args.narg() == 1) {
					String message = args.arg(0).tojstring();
					player.sendTranslatableMessage(message);
				} else if (args.narg() == 2) {
					String message = args.arg(0).tojstring();
					LuaTable table = args.arg(1).checktable();
					player.sendTranslatableMessage(message, LuaData.convertToMap(table));
				}

				return NIL;
			}
		});
	}

	@Override
	public @NotNull LuaValue toLuaValue() {
		return meta.toLuaValue();
	}
}
