package org.usfirst.frc.team192.swerve.experimental;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Timer;

public class Wheel extends Thread {

	private final int TICKS_PER_ROTATION = 8192; // should be 4096. check gearing.
	private final double LIMIT_SWITCH_READ_DELAY = 0.05;
	private final double TWO_PI = Math.PI * 2;

	private final double MIN_ANGLE_CHANGE = 0.005;
	private final double MIN_SPEED_CHANGE = 0.05;

	private CANTalon rotateMotor;
	private CANTalon driveMotor;
	private DigitalInput limitSwitch;

	private boolean running;
	private boolean reversed;

	private double targetAngle;
	private double driveSpeed;

	public Wheel(CANTalon rotateMotor, CANTalon driveMotor, DigitalInput limitSwitch) {
		this.rotateMotor = rotateMotor;
		this.driveMotor = driveMotor;
		this.limitSwitch = limitSwitch;

		rotateMotor.setFeedbackDevice(FeedbackDevice.QuadEncoder);
		rotateMotor.configEncoderCodesPerRev(TICKS_PER_ROTATION / 4);
		rotateMotor.changeControlMode(TalonControlMode.Position);
	}

	public void enable() {
		running = true;
		setDriveSpeed(0);
		setTargetPosition(0);
		targetAngle = 0;
		driveSpeed = 0;
		start();
	}

	public void disable() {
		running = false;
	}

	public Wheel copy() {
		return new Wheel(rotateMotor, driveMotor, limitSwitch);
	}

	@Override
	public void run() {
		System.out.println("enabled");
		boolean switchValue = limitSwitch.get();
		while (running) {
			boolean newSwitchValue = limitSwitch.get();
			if (newSwitchValue != switchValue) {
				boolean forward = (Math.signum(rotateMotor.getEncVelocity()) == 1.0f);
				if (forward != switchValue) {
					rotateMotor.setEncPosition(0);
				}
				switchValue = newSwitchValue;
			}
			Timer.delay(LIMIT_SWITCH_READ_DELAY);
		}
		System.out.println("disabled");
	}

	public void setTargetPosition(double radians) {
		double targetPosition = radians / TWO_PI;
		assert (targetPosition >= -0.5 && targetPosition <= 0.5);

		double currentPosition = (double) rotateMotor.getEncPosition() / TICKS_PER_ROTATION;
		boolean positionChanged = false;
		double delta = targetPosition - currentPosition;
		if (Math.abs(delta) > 0.5) {
			currentPosition += Math.signum(delta);
			positionChanged = true;
		}
		delta = targetPosition - currentPosition;
		boolean newReverse = false;
		if (Math.abs(delta) > 0.25) {
			targetPosition -= Math.signum(delta) * 0.5;
			newReverse = true;
		}
		if (Math.abs(targetPosition - targetAngle) > MIN_ANGLE_CHANGE) {
			if (positionChanged)
				rotateMotor.setEncPosition((int) (currentPosition * TICKS_PER_ROTATION));
			reversed = newReverse;
			rotateMotor.set(targetPosition);
		}
	}

	public void setDriveSpeed(double speed) {
		speed *= (reversed ? -1 : 1);
		if (speed == 0 || Math.abs(driveSpeed - speed) > MIN_SPEED_CHANGE) {
			driveMotor.set(speed);
			driveSpeed = speed;
		}
	}

}
