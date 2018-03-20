package org.usfirst.frc.team192.swerve;

import org.usfirst.frc.team192.config.Config;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.XboxController;

public abstract class SwerveBase {

	protected Wheel[] wheels;

	protected final double robotWidth;
	protected final double robotHeight;

	protected double SPEED_SCALE = 0.4;

	public SwerveBase() {
		wheels = new Wheel[4];
		wheels[0] = new Wheel("fl");
		wheels[1] = new Wheel("fr");
		wheels[2] = new Wheel("bl");
		wheels[3] = new Wheel("br");

		this.robotWidth = Config.getDouble("robot_width");
		this.robotHeight = Config.getDouble("robot_height");

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
		}
	}

	public void zero() {
		for (Wheel wheel : wheels)
			if (wheel != null)
				wheel.zero();
		System.out.println("Done zeroing");
	}

	public abstract void updateWithJoystick(XboxController input);

	public static double clip(double x) {
		return (Math.abs(x) > 0.1 ? x : 0);
	}

	public static double clipAndSquare(double x) {
		x = clip(x);
		return Math.copySign(x * x, x);
	}

	public double getMaxDistanceTraveled() {
		double max = Double.NEGATIVE_INFINITY;
		for (Wheel w : wheels) {
			max = Math.max(max, Math.abs(w.getTotalDistance()));
		}
		return max;
	}

	private int index;
	private boolean printed;

	public void controllerZero(XboxController xbox) {
		if (!printed) {
			System.out.println("zeroing " + wheels[index].getName());
			printed = true;
		}
		if (xbox.getAButtonPressed()) {
			index++;
			index %= 4;
			printed = false;
		} else if (xbox.getBButtonPressed()) {
			zero();
		}
		double x = xbox.getX(Hand.kLeft);
		wheels[index].getRotateMotor().set(ControlMode.PercentOutput, Math.copySign(x * x, x) / 2);
	}

}
