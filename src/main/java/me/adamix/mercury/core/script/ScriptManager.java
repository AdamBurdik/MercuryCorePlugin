package me.adamix.mercury.core.script;

import me.adamix.mercury.core.MercuryCorePlugin;
import me.adamix.mercury.core.script.data.LuaArgument;
import me.adamix.mercury.core.utils.FileUtils;
import org.jetbrains.annotations.NotNull;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.JsePlatform;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScriptManager {
	private final Globals globals = JsePlatform.standardGlobals();
	private final Map<String, LuaValue> scriptMap = new HashMap<>();

	public void loadScripts(@NotNull String folderPath){
		List<String> pathList = new ArrayList<>();
		FileUtils.forEachFile(folderPath, file -> file.isFile() && FileUtils.getExtension(file).equals("lua"), file -> {
			pathList.add(file.getAbsolutePath());
		});

		for (String path : pathList) {
			loadScript(path);
		}
	}

	public void loadScript(@NotNull String scriptPath) {
		String path = scriptPath.replace("\\", "/");
		String relativePath = path.split("/scripts/")[1];

		LuaValue script = globals.loadfile(path);
		script.call();
		scriptMap.put(relativePath, script);
	}

	public void runFunction(@NotNull String scriptId, @NotNull String categoryId, @NotNull String functionName, LuaArgument[] args) {
		LuaValue script = scriptMap.get(scriptId);
		LuaValue chunk = script.call();
		LuaValue category = chunk.get(categoryId);

		LuaValue function = category.get(functionName);
		if (args.length == 0) {
			function.call();
		} else if (args.length == 1) {
			function.call(args[0].toLuaValue());
		} else if (args.length == 2) {
			function.call(args[0].toLuaValue(), args[1].toLuaValue());
		} else if (args.length == 3) {
			function.call(args[0].toLuaValue(), args[1].toLuaValue(), args[2].toLuaValue());
		}
	}

	public void loadScript() {


		LuaTable table = new LuaTable();
		table.set("name", LuaValue.valueOf("ADAM_4644"));
		table.set("score", LuaValue.valueOf(69));

		LuaValue chunk = globals.loadfile(MercuryCorePlugin.getFolderPath() + "/scripts/test.lua");
		chunk.call();

		LuaValue testFunction = globals.get("test");
		if (testFunction.isfunction()) {
			testFunction.call(table);
		}
	}
}
