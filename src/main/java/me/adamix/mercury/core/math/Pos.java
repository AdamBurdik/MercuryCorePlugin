package me.adamix.mercury.core.math;

import io.papermc.paper.math.FinePosition;
import lombok.Setter;

@Setter
public class Pos implements FinePosition {
	private double x;
	private double y;
	private double z;
	private float yaw;
	private float pitch;

	public Pos(double x, double y, double z, float yaw, float pitch) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.yaw = yaw;
		this.pitch = pitch;
	}

	public Pos(double x, double y, double z) {
		this(x, y, z, 0, 0);
	}

	@Override
	public double x() {
		return x;
	}

	@Override
	public double y() {
		return y;
	}

	@Override
	public double z() {
		return z;
	}

	public float yaw() {
		return yaw;
	}

	public float pitch() {
		return pitch;
	}
}
