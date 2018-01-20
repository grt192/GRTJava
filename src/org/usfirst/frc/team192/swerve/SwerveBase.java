package org.usfirst.frc.team192.swerve;

import org.usfirst.frc.team192.config.Config;
import org.usfirst.frc.team192.robot.JoystickInput;

public abstract class SwerveBase {

	protected Wheel[] wheels;

	protected double robotWidth;
	protected double robotHeight;

	protected final double SPEED_SCALE = 1.0 / 3;
	private boolean zeroOnEnable;

	public SwerveBase() {
		this(false);
	}

	public SwerveBase(boolean zeroOnEnable) {
		this.zeroOnEnable = zeroOnEnable;

		wheels = new Wheel[4];
		wheels[0] = new Wheel("fl");
		wheels[1] = new Wheel("fr");
		wheels[2] = new Wheel("bl");
		wheels[3] = new Wheel("br");
		for (Wheel wheel : wheels)
			if (wheel != null)
				wheel.initialize();

		this.robotWidth = Config.getDouble("robot_width");
		this.robotHeight = Config.getDouble("robot_height");

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
