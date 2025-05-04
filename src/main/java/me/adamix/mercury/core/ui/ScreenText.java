package me.adamix.mercury.core.ui;

import me.adamix.mercury.api.player.MercuryPlayer;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ScreenText {
	private static final String CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ?!_.,-+=%\"':;<>[]{}()/";
	private static final Map<Character, Integer> WIDTHS = new HashMap<>();

	static {
		WIDTHS.put('f', 4);
		WIDTHS.put('i', 1);
		WIDTHS.put('k', 4);
		WIDTHS.put('l', 2);
		WIDTHS.put('t', 3);
		WIDTHS.put('I', 3);
		WIDTHS.put('!', 1);
		WIDTHS.put(',', 1);
		WIDTHS.put('.', 1);
		WIDTHS.put('"', 4);
		WIDTHS.put('\'', 1);
		WIDTHS.put(':', 1);
		WIDTHS.put(';', 1);
		WIDTHS.put('<', 4);
		WIDTHS.put('>', 4);
		WIDTHS.put('[', 3);
		WIDTHS.put(']', 3);
		WIDTHS.put('{', 4);
		WIDTHS.put('}', 4);
		WIDTHS.put('(', 4);
		WIDTHS.put(')', 4);
	}

	private static @NotNull String convertText(@NotNull String text, int lineOffset) {
		String[] lines = text.split("%nl%");
		StringBuilder builder = new StringBuilder();
		System.out.println(Arrays.toString(lines));

		int lineIndex = 1 + 180;
		for (String line : lines) {
			String lineHex = String.format("%02x", lineIndex + lineOffset);
			for (char character : line.toCharArray()) {
				int index = CHARS.indexOf(character);
				String charHex = String.format("%02x", index);
				String hex = "\\u" + lineHex + charHex;
				System.out.println("hex: " + hex);
//				builder.append("\\u" + lineHex + charHex);
//				builder.append('\u0100');
				char c = (char) Integer.parseInt(hex.substring(2), 16);
				builder.append(c);
				System.out.println(lineHex + " " + charHex);
			}
			for (char character : line.toCharArray()) {
				int width = WIDTHS.getOrDefault(character, 5);
				builder.append("\uEE00".repeat(Math.max(0, width)));
				builder.append('\uEE00');
			}

			lineIndex++;
		}

		return builder.toString();
	}

	public static void sendText(@NotNull MercuryPlayer player, @NotNull String text, int lineOffset) {
		String convertedText = convertText(text, lineOffset);

		for (BossBar activeBossBar : player.getBukkitPlayer().activeBossBars()) {
			player.getBukkitPlayer().hideBossBar(activeBossBar);
		}

		BossBar bossBar = BossBar.bossBar(
				Component.text(convertedText).font(Key.key("mercury", "font")),
				0f,
				BossBar.Color.YELLOW,
				BossBar.Overlay.PROGRESS
		);
		player.getBukkitPlayer().showBossBar(bossBar);
	}
}
