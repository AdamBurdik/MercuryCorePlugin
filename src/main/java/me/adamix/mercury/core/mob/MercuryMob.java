package me.adamix.mercury.core.mob;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import me.adamix.mercury.core.MercuryCore;
import me.adamix.mercury.core.attribute.MercuryAttribute;
import me.adamix.mercury.core.exception.MobNotSpawnedException;
import me.adamix.mercury.core.math.Pos;
import me.adamix.mercury.core.mob.attribute.MobAttributeContainer;
import me.adamix.mercury.core.mob.component.MercuryMobComponent;
import me.adamix.mercury.core.mob.component.MobAttributeComponent;
import me.adamix.mercury.core.mob.event.EventHandler;
import me.adamix.mercury.core.player.MercuryPlayer;
import me.adamix.mercury.core.protocol.data.EntityMetadata;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.minecraft.network.syncher.SynchedEntityData;
import org.bukkit.entity.Creature;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Mob;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Collectors;


@Getter
public class MercuryMob {
	@Setter(AccessLevel.PACKAGE)
	private @Nullable Mob bukkitMob;
	private final @NotNull EntityType entityType;
	private final @NotNull String name;
	private final @NotNull MercuryMobComponent[] components;
	private final @Nullable EventHandler eventHandler;

	public MercuryMob(
			@NotNull EntityType entityType,
			@NotNull String name,
			@NotNull MercuryMobComponent[] components,
			@Nullable EventHandler eventHandler
		) {
		this.entityType = entityType;
		this.name = name;
		this.components = components;
		this.eventHandler = eventHandler;
	}

	public MercuryMob(
			@NotNull EntityType entityType,
			@NotNull String name,
			@NotNull MobAttributeContainer attributes,
			@Nullable EventHandler eventHandler
		) {
		this(entityType, name, new MercuryMobComponent[]{attributes.toComponent()}, eventHandler);
	}

	public boolean hasComponent(Class<? extends MercuryMobComponent> clazz) {
		return getComponent(clazz) != null;
	}

	public <T extends MercuryMobComponent> @Nullable T getComponent(Class<T> clazz) {
		for (@NotNull MercuryMobComponent itemComponent : components) {
			if (itemComponent.getClass().equals(clazz)) {
				if (clazz.isInstance(itemComponent)) {
					return clazz.cast(itemComponent);
				}
			}
		}
		return null;
	}

	/**
	 * Retrieves bukkit mob
	 * @return {@link Mob} bukkit mob instance
	 * @throws MobNotSpawnedException if mob has not been spawned yet.
	 */
	@ApiStatus.Internal
	public @NotNull Mob getBukkitMob() {
		if (bukkitMob == null) {
			throw new MobNotSpawnedException("Cannot retrieve bukkit mob! Mob has not been spawned!");
		}
		return bukkitMob;
	}

	public double getMaxHealth() {
		MobAttributeComponent component = getComponent(MobAttributeComponent.class);
		if (component == null) {
			return 0;
		}

		Double maxHealthValue = component.get(MercuryAttribute.MAX_HEALTH);
		return maxHealthValue != null ? maxHealthValue.floatValue() : 0f;
	}

	public void updateName(@NotNull MercuryPlayer player) {
		Component component = MercuryCore.placeholderManager().parse(name, player);

		EntityMetadata metadata = new EntityMetadata(getBukkitMob())
				.setCustomName(component)
				.setCustomNameVisible(true);

		MercuryCore.protocol().sendEntityMetadata(metadata, player.getBukkitPlayer());
	}


}
