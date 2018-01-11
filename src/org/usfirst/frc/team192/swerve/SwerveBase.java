package org.usfirst.frc.team192.swerve;

import org.usfirst.frc.team192.robot.JoystickInput;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;

public abstract class SwerveBase {

	protected Wheel[] wheels;

	protected double robotWidth;
	protected double robotHeight;

	protected final double SPEED_SCALE = 1.0;

	public SwerveBase(double robotWidth, double robotHeight) {
		wheels = new Wheel[4];
		wheels[2] = new Wheel(new TalonSRX(1), new TalonSRX(2));
		wheels[3] = new Wheel(new TalonSRX(8), new TalonSRX(7));
		wheels[1] = new Wheel(new TalonSRX(9), new TalonSRX(10));
		wheels[0] = new Wheel(new TalonSRX(14), new TalonSRX(16));
		for (Wheel wheel : wheels)
			if (wheel != null)
				wheel.initialize();

		this.robotWidth = robotWidth;
		this.robotHeight = robotHeight;
	}

	public void enable() {
		for (Wheel wheel : wheels)
			if (wheel != null)
				wheel.enable();
	}

	public void disable() {
		for (int i = 0; i < wheels.length; i++) {
			if (wheels[i] == null)
				continue;
			wheels[i].disable();
			wheels[i] = wheels[i].copy();
		}
	}

	public void zero() {
		for (Wheel wheel : wheels)
			if (wheel != null)
				wheel.zero();
	}

	public double realAtan(double x, double y) {
		return (Math.atan2(y, x) + 2 * Math.PI) % (2 * Math.PI);
	}

	public abstract void update(JoystickInput input);

}