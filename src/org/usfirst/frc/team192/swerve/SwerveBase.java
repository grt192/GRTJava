package org.usfirst.frc.team192.swerve;

import org.usfirst.frc.team192.robot.JoystickInput;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;

public abstract class SwerveBase {

	protected Wheel[] wheels;

	protected double robotWidth;
	protected double robotHeight;

	protected final double SPEED_SCALE = 1.0 / 3;
	private boolean zeroOnEnable;
	
	protected TalonSRX[] rotates;
	protected TalonSRX[] drives;

	public SwerveBase(double robotWidth, double robotHeight) {
		this(robotWidth, robotHeight, false);
	}

	public SwerveBase(double robotWidth, double robotHeight, boolean zeroOnEnable) {
		this.zeroOnEnable = zeroOnEnable;
		this.rotates = new TalonSRX[] {new TalonSRX(14), new TalonSRX(9), new TalonSRX(1), new TalonSRX(8)};
		this.drives  = new TalonSRX[] {new TalonSRX(16), new TalonSRX(10), new TalonSRX(2), new TalonSRX(7)};

		wheels = new Wheel[4];
		wheels[2] = new Wheel(rotates[2], drives[2]);
		wheels[3] = new Wheel(rotates[3], drives[3]);
		wheels[1] = new Wheel(rotates[1], drives[1]);
		wheels[0] = new Wheel(rotates[0], drives[0]);
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
		if (zeroOnEnable)
			zero();
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
		System.out.println("Done zeroing");
	}

	public double realAtan(double x, double y) {
		return (Math.atan2(x, -y) + 2 * Math.PI) % (2 * Math.PI);
	}

	public abstract void update(JoystickInput input);

}
