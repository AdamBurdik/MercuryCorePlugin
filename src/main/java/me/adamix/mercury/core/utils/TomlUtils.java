package me.adamix.mercury.core.utils;

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import me.adamix.mercury.core.attribute.MercuryAttribute;
import me.adamix.mercury.core.attribute.MercuryAttributeModifier;
import me.adamix.mercury.core.toml.MercuryTable;
import net.kyori.adventure.key.Key;
import org.bukkit.attribute.AttributeModifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.tomlj.TomlTable;

import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class TomlUtils {
	public static Map<MercuryAttribute, MercuryAttributeModifier> parseAttributes(@NotNull MercuryTable table, @NotNull Key blueprintKey) {
		Set<String> keySet = table.getTomlTable().keySet();
		Map<MercuryAttribute, MercuryAttributeModifier> modifierMap = new Object2ObjectArrayMap<>();

		for (String key : keySet) {
			MercuryTable attributeTable = table.getTableSafe(key);

			Key attributeKey = attributeTable.getKey("key");
			if (attributeKey == null) {
				attributeKey = Key.key(blueprintKey.namespace(), blueprintKey.value() + "-" + key);
			}
			Object valueObject = attributeTable.getObject("value");
			double value;
			if (valueObject instanceof Number number) {
				value = number.doubleValue();
			} else {
				continue;
			}
			String stringOperation = attributeTable.getStringSafe("operation");

			MercuryAttributeModifier.Operation operation;
			MercuryAttribute attribute;
			try {
				operation = MercuryAttributeModifier.Operation.valueOf(stringOperation.toUpperCase(Locale.ROOT));
				attribute = MercuryAttribute.valueOf(key.toUpperCase(Locale.ROOT));
			} catch (IllegalArgumentException e) {
				continue;
			}

			modifierMap.put(attribute, new MercuryAttributeModifier(attributeKey, value, operation));
		}

		return modifierMap;
	}
}
