package org.usfirst.frc.team192.swerve.experimental;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Timer;

public class Wheel extends Thread {

	private final int TICKS_PER_ROTATION = 8192;// TODO: Find a more accurate value
	private final double LIMIT_SWITCH_READ_DELAY = 0.05;
	private final double TWO_PI = Math.PI * 2;

	private CANTalon rotateMotor;
	private CANTalon driveMotor;
	private DigitalInput limitSwitch;

	private boolean running;
	private boolean reversed;

	private int limitSwitchPosition;
	private boolean foundLimitSwitch;

	public Wheel(CANTalon rotateMotor, CANTalon driveMotor, DigitalInput limitSwitch) {
		this.rotateMotor = rotateMotor;
		this.driveMotor = driveMotor;
		this.limitSwitch = limitSwitch;

		rotateMotor.setFeedbackDevice(FeedbackDevice.QuadEncoder);
		rotateMotor.configEncoderCodesPerRev(TICKS_PER_ROTATION);
		rotateMotor.changeControlMode(TalonControlMode.Position);
	}

	public void enable() {
		running = true;
		rotateMotor.setEncPosition(0);
		foundLimitSwitch = false;
		start();
	}

	public void disable() {
		running = false;
	}

	@Override
	public void run() {
		boolean switchValue = limitSwitch.get();
		while (running) {
			boolean newSwitchValue = limitSwitch.get();
			if (newSwitchValue != switchValue) {
				boolean forward = ((int) (Math.signum(rotateMotor.getEncVelocity())) == 1);
				if (forward != switchValue) {
					if (!foundLimitSwitch)
						limitSwitchPosition = rotateMotor.getEncPosition();
					else
						rotateMotor.setEncPosition(limitSwitchPosition);
				}
				switchValue = newSwitchValue;
			}
			Timer.delay(LIMIT_SWITCH_READ_DELAY);
		}
	}

	public void setTargetPosition(double radians) {
		double targetPosition = radians / TWO_PI;
		double position = ((double) rotateMotor.getEncPosition()) / TICKS_PER_ROTATION;
		double delta = Math.abs(position - targetPosition);
		if (delta > 0.25 && delta < 0.75) {
			reversed = true;
			targetPosition += 0.5;
		} else {
			reversed = false;
		}
	}

	public void setDriveSpeed(double speed) {
		int reverseFactor = reversed ? -1 : 1;
		driveMotor.set(speed * reverseFactor);
	}

}
