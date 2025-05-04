package me.adamix.mercury.core.data;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import me.adamix.mercury.api.data.DataInstance;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

@Setter
@Getter
@ToString
public class DummyData implements DataInstance {
	private int score;

	public DummyData(int score) {
		this.score = score;
	}

	@Override
	public @NotNull Map<String, Object> serialize() {
		return Map.of("score", score);
	}
}
