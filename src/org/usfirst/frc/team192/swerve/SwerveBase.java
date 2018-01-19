package org.usfirst.frc.team192.swerve;

import org.usfirst.frc.team192.config.Config;
import org.usfirst.frc.team192.robot.JoystickInput;

public abstract class SwerveBase {

	protected Wheel[] wheels;

	protected double robotWidth;
	protected double robotHeight;

	protected final double SPEED_SCALE = 1.0 / 3;
	private boolean zeroOnEnable;

	public SwerveBase(Config config) {
		this(false, config);
	}

	public SwerveBase(boolean zeroOnEnable, Config config) {
		this.zeroOnEnable = zeroOnEnable;

		wheels = new Wheel[4];
		wheels[0] = new Wheel("tl", config);
		wheels[1] = new Wheel("tr", config);
		wheels[2] = new Wheel("bl", config);
		wheels[0] = new Wheel("br", config);
		for (Wheel wheel : wheels)
			if (wheel != null)
				wheel.initialize();

		this.robotWidth = config.getDouble("robot_width");
		this.robotHeight = config.getDouble("robot_height");

	}

	public void enable() {
		for (Wheel wheel : wheels)
			if (wheel != null)
				wheel.enable();
		if (zeroOnEnable)
			zero();
	}

	public void disable() {
		for (int i = 0; i < wheels.length; i++) {
			if (wheels[i] == null)
				continue;
			wheels[i].disable();
		}
	}

	public void zero() {
		for (Wheel wheel : wheels)
			if (wheel != null)
				wheel.zero();
		System.out.println("Done zeroing");
	}

	public double realAtan(double x, double y) {
		return (Math.atan2(x, -y) + 2 * Math.PI) % (2 * Math.PI);
	}

	public abstract void update(JoystickInput input);

}
