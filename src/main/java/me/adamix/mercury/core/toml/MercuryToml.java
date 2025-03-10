package me.adamix.mercury.core.toml;


import io.papermc.paper.math.FinePosition;
import me.adamix.mercury.core.math.Pos;
import net.kyori.adventure.key.Key;
import net.minecraft.core.Position;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.intellij.lang.annotations.Subst;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.tomlj.TomlArray;
import org.tomlj.TomlTable;

import java.util.Objects;

public abstract class MercuryToml {

	public abstract void mustContain(@NotNull String dottedKey);
	public abstract @Nullable String getString(@NotNull String dottedKey);
	public abstract @Nullable Boolean getBoolean(@NotNull String dottedKey);
	public abstract @Nullable Integer getInteger(@NotNull String dottedKey);
	public abstract @Nullable Float getFloat(@NotNull String dottedKey);
	public abstract @Nullable Double getDouble(@NotNull String dottedKey);
	public abstract @Nullable Long getLong(@NotNull String dottedKey);
	public abstract @Nullable MercuryTable getTable(@NotNull String dottedKey);
	public abstract @Nullable MercuryArray getArray(@NotNull String dottedKey);
	public abstract @Nullable TomlTable getTomlTable(@NotNull String dottedKey);
	public abstract @Nullable TomlArray getTomlArray(@NotNull String dottedKey);
	public abstract @Nullable Pos getPos(@NotNull String dottedKey);
	public abstract @NotNull String getName();

	public @Nullable Key getKey(@NotNull String dottedKey) {
		String value = getString(dottedKey);
		if (value == null) {
			return null;
		}
		return Key.key(value);
	}

	public @NotNull Key getKeySafe(@NotNull String dottedKey) {
		mustContain(dottedKey);
		Key value = getKey(dottedKey);
		Objects.requireNonNull(value);
		return value;
	}

	public @Nullable Pos getPos(@Nullable TomlArray tomlArray) {
		if (tomlArray == null) {
			return null;
		}
		if (tomlArray.size() != 3 && tomlArray.size() != 5) {
			return null;
		}
		double x = tomlArray.getDouble(0);
		double y = tomlArray.getDouble(1);
		double z = tomlArray.getDouble(2);
		float yaw = 0f;
		float pitch = 0f;
		if (tomlArray.size() == 5) {
			yaw = (float) tomlArray.getDouble(3);
			pitch = (float) tomlArray.getDouble(4);
		}
		return new Pos(x, y, z, yaw, pitch);
	}

	public @Nullable Pos getPosSafe(@NotNull TomlArray tomlArray) {
		if (tomlArray.size() != 3 && tomlArray.size() != 5) {
			throw new RuntimeException("Invalid position in " + getName() + "! Array with position must have 3 (x, y, z) or 5 (x, y, z, yaw, pitch) values!");
		}
		double x = tomlArray.getDouble(0);
		double y = tomlArray.getDouble(1);
		double z = tomlArray.getDouble(2);
		float yaw = 0f;
		float pitch = 0f;
		if (tomlArray.size() == 5) {
			yaw = (float) tomlArray.getDouble(3);
			pitch = (float) tomlArray.getDouble(4);
		}
		return new Pos(x, y, z, yaw, pitch);
	}


	public @NotNull Pos getPosSafe(@NotNull String dottedKey) {
		mustContain(dottedKey);
		Pos value = getPos(dottedKey);
		Objects.requireNonNull(value);
		return value;
	}

	public @Nullable Material getMaterial(@NotNull String dottedKey) {
		String value = getString(dottedKey);
		if (value == null) {
			return null;
		}
		return Material.matchMaterial(value);
	}

	public @NotNull Material getMaterialSafe(@NotNull String dottedKey) {
		mustContain(dottedKey);
		Material value = getMaterial(dottedKey);
		Objects.requireNonNull(value);
		return value;
	}

	public @Nullable EntityType getEntityType(@NotNull String dottedKey) {
		String value = getString(dottedKey);
		if (value == null) {
			return null;
		}
		return EntityType.fromName(value);
	}

	public @NotNull EntityType getEntityTypeSafe(@NotNull String dottedKey) {
		mustContain(dottedKey);
		EntityType value = getEntityType(dottedKey);
		Objects.requireNonNull(value);
		return value;
	}
}
