package org.usfirst.frc.team192.swerve;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.DigitalInput;

public class Wheel {
	private double robotWidth;
	private double robotHeight;
	private double deltaTheta;
	private int positiveX;
	private int positiveY;

	private TalonSRX drive;
	private TalonSRX turn;
	private DigitalInput limitSwitch;

	public Wheel(double robotWidth, double robotHeight, int positiveX, int positiveY, TalonSRX drive, TalonSRX turn,
			DigitalInput limitSwitch) {
		this.robotWidth = robotWidth;
		this.robotHeight = robotHeight;
		this.deltaTheta = Math.atan(robotHeight / robotWidth);
		this.positiveX = positiveX; // 1 or -1
		this.positiveY = positiveY; // 1 or -1
		// (positiveX, positiveY) = (1, 1) => upper right
		// = (-1, 1) => upper left
		// = (1, -1) => lower right
		// = (-1, -1) => lower left

		this.drive = drive;
		this.turn = turn;
		this.limitSwitch = limitSwitch;
	}

	public double realAtan(double x, double y) {
		double first = Math.atan(y / x); // tentative result
		// int xNorm = (int) (x / Math.abs(x));
		int yNorm = (int) (y / Math.abs(y));
		if (x >= 0) {
			return first + Math.PI + (Math.PI / -2) * yNorm;
		} else {
			return first + (3 * Math.PI / 4) + (Math.PI / -4) * yNorm;
		}
	}

	public WheelVelocity update(double robotTheta, double rv, double vx, double vy) {
		double wheelCurrentTheta = robotTheta + deltaTheta;
		double r = Math.sqrt(robotWidth * robotWidth / 4 + robotHeight * robotHeight / 4);
		double dx = (r * Math.cos(wheelCurrentTheta)) * positiveX;
		double dy = (r * Math.sin(wheelCurrentTheta)) * positiveY;
		double actualvx = vx + rv * dy;
		double actualvy = vy - rv * dx;
		double wheelTheta = realAtan(actualvx, actualvy);
		double speed = Math.sqrt(actualvx * actualvx + actualvy * actualvy);
		return new WheelVelocity(speed, wheelTheta);
	}
}
