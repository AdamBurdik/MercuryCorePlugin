package me.adamix.mercury.core.script.data;

import org.jetbrains.annotations.NotNull;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.VarArgFunction;
import org.luaj.vm2.lib.ZeroArgFunction;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public class LuaData {
	public static void bind(LuaTable meta, String name, Supplier<LuaValue> supplier) {
		meta.set(name, new ZeroArgFunction() {
			@Override
			public LuaValue call() {
				return supplier.get();
			}
		});
	}

	public static void bind(LuaTable meta, String name, Function<LuaValue, LuaValue> func) {
		meta.set(name, new OneArgFunction() {
			@Override
			public LuaValue call(LuaValue luaValue) {
				return func.apply(luaValue);
			}
		});
	}

	public static void bind(LuaTable meta, String name, BiFunction<LuaValue, LuaValue, LuaValue> func) {
		meta.set(name, new TwoArgFunction() {
			@Override
			public LuaValue call(LuaValue luaValue, LuaValue luaValue1) {
				return func.apply(luaValue, luaValue1);
			}
		});
	}

	public static void bind(LuaTable meta, String name, VarArgFunction func) {
		meta.set(name, func);
	}
	
	public static void bind(LuaTable meta, String name, String value) {
		meta.set(name, value);
	}

	public static @NotNull Map<String, Object> convertToMap(@NotNull LuaTable table) {
		Map<String, Object> map = new HashMap<>();
		LuaValue k = LuaValue.NIL;
		while (true) {
			k = table.next(k).arg1();
			if (k.isnil()) break;
			LuaValue v = table.get(k);
			map.put(k.tojstring(), v.tostring());
		}
		return map;
	}
}
