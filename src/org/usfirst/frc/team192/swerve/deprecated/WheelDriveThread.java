package org.usfirst.frc.team192.swerve.deprecated;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

public class WheelDriveThread extends Thread {
	private TalonSRX motor;
	private double speed;
	public boolean shouldStillRun;

	public WheelDriveThread(TalonSRX motor) {
		this.motor = motor;
		speed = 0;
		shouldStillRun = true;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	@Override
	public void run() {
		while (true) {
			if (shouldStillRun)
				motor.set(ControlMode.PercentOutput, speed);
		}
	}
}
