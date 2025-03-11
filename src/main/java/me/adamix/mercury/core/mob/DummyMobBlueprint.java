package me.adamix.mercury.core.mob;

import me.adamix.mercury.core.attribute.MercuryAttribute;
import me.adamix.mercury.core.mob.attribute.MobAttributeContainer;
import me.adamix.mercury.core.mob.blueprint.MercuryMobBlueprint;
import org.bukkit.entity.EntityType;

public class DummyMobBlueprint extends MercuryMobBlueprint {
	public DummyMobBlueprint() {
		super(
				EntityType.ZOMBIE,
				"Dummy Mob",
				new MobAttributeContainer()
						.set(MercuryAttribute.DAMAGE, 100d)
		);
	}
}
