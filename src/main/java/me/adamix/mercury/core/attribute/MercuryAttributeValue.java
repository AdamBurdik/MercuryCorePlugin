package me.adamix.mercury.core.attribute;


import org.bukkit.attribute.AttributeModifier;

public record MercuryAttributeValue(double value, AttributeModifier.Operation operation) {
}
