package me.adamix.mercury.core.utils;

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import me.adamix.mercury.api.attribute.MercuryAttribute;
import me.adamix.mercury.api.attribute.MercuryAttributeModifier;
import me.adamix.mercury.api.configuration.MercuryTable;
import me.adamix.mercury.core.attribute.CoreMercuryAttributeModifier;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class TomlUtils {
	public static Map<MercuryAttribute, MercuryAttributeModifier> parseAttributes(@NotNull MercuryTable table, @NotNull Key blueprintKey) {
		Set<String> keySet = table.keySet();
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

			modifierMap.put(attribute, new CoreMercuryAttributeModifier(attributeKey, value, operation));
		}

		return modifierMap;
	}
}
