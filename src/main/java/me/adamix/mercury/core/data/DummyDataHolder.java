package me.adamix.mercury.core.data;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class DummyDataHolder extends DataHolder<DummyData> {
	public DummyDataHolder() {
		super("dummy");
	}

	@Override
	public @NotNull DummyData createDefault() {
		return new DummyData(0);
	}

	@Override
	public @NotNull DummyData deserialize(@NotNull Map<String, Object> data) {
		return new DummyData((int) data.get("score"));
	}
}
