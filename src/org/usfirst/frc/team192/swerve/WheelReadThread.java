package org.usfirst.frc.team192.swerve;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.DigitalInput;

public class WheelReadThread extends Thread {
	private TalonSRX turnMotor;
	private DigitalInput limitSwitch;

	private double theta;
	// private int lastUpdated;
	private boolean limitSwitchIsInTheMidstOfBeingActivated;
	private boolean zeroing;
	public int deltaEnc;
	private int startPos;

	private double TO_RADIANS;

	public WheelReadThread(TalonSRX turnMotor, DigitalInput limitSwitch) {
		this.turnMotor = turnMotor;
		this.limitSwitch = limitSwitch;
		theta = 0;
		// lastUpdated = 0;
		limitSwitchIsInTheMidstOfBeingActivated = false;

		TO_RADIANS = 0.0008359831298535551;

		zeroing = false;
		deltaEnc = 0;
	}

	@Override
	public void run() {
		while (true) {
			if (zeroing && limitSwitch.get()) {
				System.out.println("stopping it from zeroing");
				turnMotor.set(ControlMode.PercentOutput, 0);
				zeroing = false;
				// lastUpdated = turnMotor.getSensorCollection().getQuadraturePosition();
			} else if (limitSwitch.get() && !limitSwitchIsInTheMidstOfBeingActivated) {
				deltaEnc = turnMotor.getSensorCollection().getQuadraturePosition() - startPos;
				System.out.println("hit limit at " + turnMotor.getSensorCollection().getQuadraturePosition());
				limitSwitchIsInTheMidstOfBeingActivated = true;
			} else if (!limitSwitch.get()) {
				int moddedEncoderValue = turnMotor.getSensorCollection().getQuadraturePosition() - deltaEnc;
				double toBeTheta = moddedEncoderValue * TO_RADIANS;
				theta = ((toBeTheta % (2 * Math.PI)) + 2 * Math.PI) % (2 * Math.PI);
				limitSwitchIsInTheMidstOfBeingActivated = false;
			}
		}
	}

	public void zero() {
		// turnMotor.set(0.2);
		// zeroing = true;
		theta = 0;
		startPos = turnMotor.getSensorCollection().getQuadraturePosition();
		deltaEnc = startPos;
		limitSwitchIsInTheMidstOfBeingActivated = true;
	}

	public boolean isZeroing() {
		return zeroing;
	}

	public double getTheta() {
		return theta;
	}
}
