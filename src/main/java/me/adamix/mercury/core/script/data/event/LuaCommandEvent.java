package me.adamix.mercury.core.script.data.event;

import me.adamix.mercury.core.script.data.LuaArgument;
import org.bukkit.event.server.ServerCommandEvent;
import org.jetbrains.annotations.NotNull;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;

import static me.adamix.mercury.core.script.data.LuaData.bind;

public class LuaCommandEvent implements LuaArgument {
	private final LuaTable meta;

	public LuaCommandEvent(@NotNull ServerCommandEvent event) {
		this.meta = new LuaTable();
		bindAll(event);
	}

	public void bindAll(@NotNull ServerCommandEvent event) {
		// meta.bind("test", "something") Rework?
		bind(meta, "name", event.getCommand());

		bind(meta, "set_cancelled", (arg) -> {
			boolean cancelled = arg.toboolean();
			event.setCancelled(true);
			return LuaValue.NIL;
		});
	}

	@Override
	public @NotNull LuaValue toLuaValue() {
		return meta.toLuaValue();
	}
}
