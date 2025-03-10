package me.adamix.mercury.core.utils;

import me.adamix.mercury.core.attribute.MercuryAttributeValue;
import org.bukkit.attribute.AttributeModifier;
import org.jetbrains.annotations.Nullable;
import org.tomlj.TomlTable;

public class TomlUtils {
	public static @Nullable MercuryAttributeValue parseAttribute(TomlTable table, String fileName) {
		if (!table.contains(fileName)) {
			return null;
		}

		Object attributeObject = table.get(fileName);

		if (attributeObject instanceof Number number) {
			return new MercuryAttributeValue(number.floatValue(), AttributeModifier.Operation.ADD_NUMBER);
		} else if (attributeObject instanceof TomlTable attributeTable) {
			Double doubleValue = attributeTable.getDouble("value");
			if (doubleValue == null) {
				return null;
			}
			double value = doubleValue;

			String stringOperation = attributeTable.getString("operation");
			if (stringOperation == null) {
				return null;
			}
			AttributeModifier.Operation operation;
			try {
				operation = AttributeModifier.Operation.valueOf(stringOperation);
			} catch (IllegalArgumentException e) {
				return null;
			}

			return new MercuryAttributeValue((float) value, operation);
		}

		return null;
	}
}
