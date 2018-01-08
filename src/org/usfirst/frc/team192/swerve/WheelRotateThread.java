package org.usfirst.frc.team192.swerve;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

public class WheelRotateThread extends Thread {
	/*
	 * private TalonSRX frontRight; private TalonSRX frontLeft; private TalonSRX
	 * backRight; private TalonSRX backLeft;
	 */
	private TalonSRX[] motors = new TalonSRX[4];
	private WheelReadThread wheelRead;
	private double targetTheta;
	public boolean shouldStillRun;
	public boolean changingModes;
	private boolean moving;
	private long[] timeItShouldStop;
	private int multiplier;

	public double TOLERANCE; // radians

	public WheelRotateThread(TalonSRX frontRight, TalonSRX frontLeft, TalonSRX backRight, TalonSRX backLeft,
			WheelReadThread wheelRead) {
		motors[0] = frontRight;
		motors[1] = frontLeft;
		motors[2] = backRight;
		motors[3] = backLeft;

		this.wheelRead = wheelRead;
		this.targetTheta = 0;
		TOLERANCE = 0.02;
		shouldStillRun = true;

		changingModes = false;
		moving = false;
		timeItShouldStop = new long[4];
		multiplier = 1;
	}

	public void setTargetTheta(double targetTheta) {
		// System.out.println("setting target theta to " + targetTheta);
		this.targetTheta = targetTheta;
	}

	private void setAllMotors(double val) {
		for (int i = 0; i < motors.length; i++) {
			motors[i].set(ControlMode.PercentOutput, val);
		}
	}

	public void setModeToRotate(double width, double height) {
		double initialTheta = Math.atan2(height, width);
		setTargetTheta(initialTheta);
		long now = System.currentTimeMillis();
		timeItShouldStop[0] = now + 0; // how long should front right run
		timeItShouldStop[1] = now + 0; // how long should front left run
		timeItShouldStop[2] = now;
		timeItShouldStop[3] = now + 0; // how long should back left run
		setAllMotors(0.5);
		changingModes = true;
	}

	public void setModeToStrafe(double width, double height) {
		setTargetTheta(0);
		long now = System.currentTimeMillis();
		timeItShouldStop[0] = now + 0; // how long should front right run
		timeItShouldStop[1] = now + 0; // how long should front left run
		timeItShouldStop[2] = now;
		timeItShouldStop[3] = now + 0; // how long should back left run
		setAllMotors(-0.5);
		changingModes = true;
	}

	@Override
	public void run() {
		while (true) {
			if (shouldStillRun) {
				double current = wheelRead.getTheta();
				double forwardChange = ((targetTheta - current) % (2 * Math.PI) + 2 * Math.PI) % (2 * Math.PI);
				double backwardChange = (-forwardChange + 2 * Math.PI) % (2 * Math.PI);
				// System.out.println(targetTheta);
				if (changingModes) {
					changingModes = false;
					long now = System.currentTimeMillis();
					for (int i = 0; i < motors.length; i++) {
						if (now > timeItShouldStop[i]) {
							motors[i].set(ControlMode.PercentOutput, 0);
						} else {
							changingModes = true;
						}
					}
				} else if (Math.min(forwardChange, backwardChange) < TOLERANCE) // kind of a waste to keep changing if
																				// it's about right already
				{
					// System.out.println("not moving motor");
					setAllMotors(0);
					moving = false;
				} else if (forwardChange < backwardChange) // if it would be shorter to move forward than backward, move
															// forward
				{
					setAllMotors(forwardChange / Math.PI);
					moving = true;
				} else {
					setAllMotors(-backwardChange / Math.PI);
					moving = true;
				}

				// System.out.println("target: " + targetTheta);
				// System.out.println(motor.getDeviceID() + " encoder value: " + current);
			} else {
				setAllMotors(0);
			}
		}
	}
}
