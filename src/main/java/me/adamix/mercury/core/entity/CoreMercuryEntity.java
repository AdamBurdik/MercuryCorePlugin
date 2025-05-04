package me.adamix.mercury.core.entity;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import me.adamix.mercury.api.attribute.AttributeContainer;
import me.adamix.mercury.api.entity.MercuryEntity;
import me.adamix.mercury.api.entity.component.MercuryEntityComponent;
import me.adamix.mercury.api.entity.type.MercuryEntityType;
import me.adamix.mercury.api.math.MercuryPosition;
import me.adamix.mercury.api.player.MercuryPlayer;
import net.kyori.adventure.key.Key;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.UUID;

@SuppressWarnings("UnstableApiUsage")
public class CoreMercuryEntity implements MercuryEntity {
	private @Nullable Entity entity;
	private final @Nullable Key blueprintKey;
	private final @NotNull MercuryEntityType type;
	private @Nullable World world;
	private @Nullable MercuryPosition position;
	private final Set<MercuryPlayer> viewers = new ObjectOpenHashSet<>();
	private final Set<MercuryEntityComponent> components;
	private long health;
	private long maxHealth;
	private final long flags;
	private final @Nullable AttributeContainer attributeContainer;

	public CoreMercuryEntity(
			@Nullable Key blueprintKey,
			@NotNull MercuryEntityType type,
			long health,
			long maxHealth,
			long flags,
			@Nullable AttributeContainer attributeContainer,
			@NotNull Set<MercuryEntityComponent> components
	) {
		this.blueprintKey = blueprintKey;
		this.type = type;
		this.health = health;
		this.maxHealth = maxHealth;
		this.flags = flags;
		this.attributeContainer = attributeContainer;
		this.components = components;
	}

	// In future will be used for recreating entities. (After server start)
	public CoreMercuryEntity(
			@Nullable Key blueprintKey,
			@NotNull MercuryEntityType type,
			@Nullable World world,
			@Nullable MercuryPosition position,
			long health,
			long maxHealth,
			long flags,
			@Nullable AttributeContainer attributeContainer,
			@NotNull Set<MercuryEntityComponent> components
	) {
		this.blueprintKey = blueprintKey;
		this.type = type;
		this.world = world;
		this.position = position;
		this.health = health;
		this.maxHealth = maxHealth;
		this.flags = flags;
		this.attributeContainer = attributeContainer;
		this.components = components;
	}

	@Override
	public @Nullable Entity bukkitEntity() {
		return entity;
	}

	public @NotNull MercuryEntityType type() {
		return type;
	}

	@Override
	public @Nullable Key getBlueprintKey() {
		return blueprintKey;
	}

	@Override
	public @Nullable UUID getUuid() {
		return entity != null ? entity.getUniqueId() : null;
	}

	@Override
	public @Nullable World getWorld() {
		return world;
	}

	@Override
	public @Nullable MercuryPosition getPosition() {
		return position;
	}

	@ApiStatus.Internal
	@Override
	public void setWorld(@NotNull World world, @NotNull MercuryPosition mercuryPosition) {
		if (this.position == null) {
			this.position = mercuryPosition;
		}
		if (this.world == null) {
			this.world = world;
		}

		Location location = new Location(this.world, position.x(), position.y(), position.z(), position.pitch(), position.yaw());
		EntityType bukkitType = EntityType.fromName(type.getName());

		this.entity = world.spawn(location, bukkitType.getEntityClass(), false, entity -> {
			// ToDO Apply some stuff in here
		});

		spawn();
	}

	@Override
	public void spawn() {
		// ToDO Call some event from here?
	}

	@Override
	public void load() {
		// ToDO Call some event from here?
	}

	@Override
	public boolean isAlive() {
		return entity != null;
	}

	@Override
	public @NotNull Set<MercuryPlayer> getViewers() {
		return viewers;
	}

	@Override
	public void addViewer(@NotNull MercuryPlayer player) {
		viewers.add(player);
	}

	@Override
	public void removeViewer(@NotNull MercuryPlayer player) {
		viewers.remove(player);
	}

	@Override
	public @NotNull Set<MercuryEntityComponent> getComponents() {
		return components;
	}

	@Override
	public void addComponent(@NotNull MercuryEntityComponent component) {
		components.add(component);
	}

	@Override
	public <T extends MercuryEntityComponent> @Nullable T getComponent(@NotNull Class<T> clazz) {
		for (MercuryEntityComponent component : components) {
			if (component.getClass().equals(clazz)) {
				//noinspection unchecked
				return (T) component;
			}
		}
		return null;
	}

	@Override
	public <T extends MercuryEntityComponent> void removeComponent(@NotNull Class<T> clazz) {
		components.removeIf(component -> component.getClass().equals(clazz));
	}

	@Override
	public long getHealth() {
		return health;
	}

	@Override
	public long getMaxHealth() {
		return maxHealth;
	}

	@Override
	public void setHealth(long value) {
		this.health = Math.min(value, maxHealth);
	}

	@Override
	public void setMaxHealth(long value) {
		this.maxHealth = value;
	}

	@Override
	public void changeHealth(long amount) {
		setHealth(health + amount);
	}

	@Override
	public @Nullable AttributeContainer attributeContainer() {
		return attributeContainer;
	}

	@Override
	public long flags() {
		return flags;
	}
}
