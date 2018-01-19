package org.usfirst.frc.team192.swerve;

import org.usfirst.frc.team192.config.Config;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.DigitalInput;

class Wheel {

	private final double TICKS_PER_ROTATION;
	private final double TWO_PI = Math.PI * 2;

	private final double MIN_ANGLE_CHANGE = 0.005;
	private final double MIN_SPEED_CHANGE = 0.05;

	private TalonSRX rotateMotor;
	private TalonSRX driveMotor;
	private DigitalInput limitSwitch;

	public boolean reversed;

	private double targetAngle;
	private double driveSpeed;

	public Wheel(String name, Config config) {
		rotateMotor = new TalonSRX(config.getInt(name + "_rotate_port"));
		driveMotor = new TalonSRX(config.getInt(name + "_drive_port"));
		int dioPort = config.getInt(name + "_dio_port");
		if (dioPort != -1)
			limitSwitch = new DigitalInput(dioPort);
		else
			limitSwitch = null;

		TICKS_PER_ROTATION = config.getDouble("ticks_per_rotation");
	}

	public void initialize() {
		rotateMotor.setNeutralMode(NeutralMode.Brake);
		driveMotor.setNeutralMode(NeutralMode.Brake);
		rotateMotor.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 0);
		rotateMotor.config_kP(0, 1.0, 0);
		rotateMotor.config_kI(0, 0.0, 0);
		rotateMotor.config_kD(0, 0.0, 0);

		rotateMotor.config_kP(1, 0.0, 0);
		rotateMotor.config_kI(1, 0.0, 0);
		rotateMotor.config_kD(1, 0.0, 0);
		zero();
	}

	public void enable() {
		rotateMotor.set(ControlMode.Disabled, 0);
		targetAngle = rotateMotor.getSelectedSensorPosition(0) / TICKS_PER_ROTATION;
		setDriveSpeed(0);
		driveSpeed = 0;
		reversed = false;
	}

	public void zero() {
		rotateMotor.getSensorCollection().setQuadraturePosition(0, 0);
	}

	public void disable() {
		rotateMotor.set(ControlMode.Disabled, 0);
		driveMotor.set(ControlMode.Disabled, 0);
	}

	public void setTargetPosition(double radians) {
		double targetPosition = radians / TWO_PI;
		targetPosition = ((targetPosition % 1.0) + 1.0) % 1.0;

		int encoderPosition = rotateMotor.getSelectedSensorPosition(0);
		double currentPosition = encoderPosition / TICKS_PER_ROTATION;
		double rotations = Math.floor(currentPosition);
		currentPosition -= rotations;
		double delta = currentPosition - targetPosition;
		if (Math.abs(delta) > 0.5) {
			targetPosition += Math.signum(delta);
		}
		delta = currentPosition - targetPosition;
		boolean newReverse = false;
		if (Math.abs(delta) > 0.25) {
			targetPosition += Math.signum(delta) * 0.5;
			newReverse = true;
		}
		targetPosition += rotations;
		if (Math.abs(targetPosition - targetAngle) > MIN_ANGLE_CHANGE || newReverse != reversed) {
			reversed = newReverse;
			targetAngle = targetPosition;
			double encoderPos = targetPosition * TICKS_PER_ROTATION;
			rotateMotor.set(ControlMode.Position, encoderPos);
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
