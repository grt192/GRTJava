package org.usfirst.frc.team192.swerve.experimental;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Timer;

public class Wheel extends Thread {

	private final int TICKS_PER_ROTATION = 8192; // should be 4096. check gearing.
	private final double LIMIT_SWITCH_READ_DELAY = 0.05;
	private final double TWO_PI = Math.PI * 2;

	private final double MIN_ANGLE_CHANGE = 0.005;
	private final double MIN_SPEED_CHANGE = 0.05;

	private TalonSRX rotateMotor;
	private TalonSRX driveMotor;
	private DigitalInput limitSwitch;
	private boolean useLimitSwitch;

	private boolean running;
	private boolean reversed;

	private double targetAngle;
	private double driveSpeed;
	private double offset;

	public Wheel(TalonSRX rotateMotor, TalonSRX driveMotor, DigitalInput limitSwitch) {
		this.rotateMotor = rotateMotor;
		this.driveMotor = driveMotor;
		this.limitSwitch = limitSwitch;

		useLimitSwitch = (limitSwitch != null);
	}

	public void initialize() {
		rotateMotor.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 0);
		rotateMotor.config_kP(0, 1.0, 0);
		rotateMotor.config_kI(0, 0.0, 0);
		rotateMotor.config_kD(0, 0.0, 0);
	}

	public void enable() {
		running = true;
		setDriveSpeed(0);
		setTargetPosition(0);
		targetAngle = 0;
		driveSpeed = 0;
		start();
	}

	public void zero() {
		offset = rotateMotor.getSelectedSensorPosition(0) / TICKS_PER_ROTATION;
	}

	public void disable() {
		running = false;
	}

	public Wheel copy() {
		Wheel wheel = new Wheel(rotateMotor, driveMotor, limitSwitch);
		wheel.offset = offset;
		return wheel;
	}

	@Override
	public void run() {
		boolean switchValue = false;
		if (useLimitSwitch)
			switchValue = limitSwitch.get();
		while (running) {
			if (useLimitSwitch) {
				boolean newSwitchValue = limitSwitch.get();
				if (newSwitchValue != switchValue) {
					boolean forward = (Math.signum(rotateMotor.getSelectedSensorVelocity(0)) == 1.0f);
					if (forward != switchValue) {
						rotateMotor.getSensorCollection().setQuadraturePosition(0, 0);
					}
					switchValue = newSwitchValue;
				}
			} else {
				int position = rotateMotor.getSelectedSensorPosition(0);
				if (position > TICKS_PER_ROTATION || position < -TICKS_PER_ROTATION) {
					System.out.println(position + ", " + position % TICKS_PER_ROTATION);
					rotateMotor.getSensorCollection().setQuadraturePosition(position % TICKS_PER_ROTATION, 0);
				}
			}
			Timer.delay(LIMIT_SWITCH_READ_DELAY);
		}
	}

	public void setTargetPosition(double radians) {
		double targetPosition = radians / TWO_PI;
		targetPosition += offset;
		while (targetPosition < -0.5)
			targetPosition++;
		while (targetPosition > 0.5)
			targetPosition--;

		double currentPosition = (double) rotateMotor.getSelectedSensorPosition(0) / TICKS_PER_ROTATION;
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
				rotateMotor.getSensorCollection().setQuadraturePosition((int) (currentPosition * TICKS_PER_ROTATION),
						0);
			reversed = newReverse;
			targetAngle = targetPosition;
			rotateMotor.set(ControlMode.Position, targetPosition * TICKS_PER_ROTATION);
		}
	}

	public void setDriveSpeed(double speed) {
		speed *= (reversed ? -1 : 1);
		if ((speed == 0.0 && driveSpeed != 0.0) || Math.abs(driveSpeed - speed) > MIN_SPEED_CHANGE) {
			driveMotor.set(ControlMode.PercentOutput, speed);
			driveSpeed = speed;
		}
	}

}
