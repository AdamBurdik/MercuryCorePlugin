package me.adamix.mercury.core.script.data;

import org.jetbrains.annotations.NotNull;
import org.luaj.vm2.LuaValue;

public interface LuaArgument {
	@NotNull LuaValue toLuaValue();
}
