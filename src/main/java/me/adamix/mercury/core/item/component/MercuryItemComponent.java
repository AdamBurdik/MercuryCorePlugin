package me.adamix.mercury.core.item.component;


import java.util.Map;

public interface MercuryItemComponent {
	String name();
	Map<String, Object> serialize();
}
