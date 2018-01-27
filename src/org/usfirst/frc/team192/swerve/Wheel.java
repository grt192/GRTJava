package org.usfirst.frc.team192.swerve;

import org.usfirst.frc.team192.config.Config;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

class Wheel {

	private final double TICKS_PER_ROTATION;
	private final int OFFSET;
	private final double DRIVE_TICKS_TO_MPS;

	private static final double TWO_PI = Math.PI * 2;

	private final double MIN_ANGLE_CHANGE = 0.005;
	private final double MIN_SPEED_CHANGE = 0.05;

	private static final double kP = 9000.0;
	private static final double kI = 0.0;
	private static final double kD = 0.0;
	private static final double maxIAccum = 0.0;

	private TalonSRX rotateMotor;
	private TalonSRX driveMotor;

	private String name;

	public boolean reversed;

	private double targetAngle;
	private double driveSpeed;

	public Wheel(String name) {
		this.name = name;

		rotateMotor = new TalonSRX(Config.getInt(name + "_rotate_port"));
		driveMotor = new TalonSRX(Config.getInt(name + "_drive_port"));
		TICKS_PER_ROTATION = Config.getDouble("ticks_per_rotation");
		OFFSET = Config.getInt(name + "_offset");
		DRIVE_TICKS_TO_MPS = Config.getDouble("drive_encoder_scale");

		FeedbackDevice feedbackDevice;
		switch (Config.getString("feedback_device")) {
		case "Analog":
			feedbackDevice = FeedbackDevice.Analog;
			break;
		case "QuadEncoder":
		default:
			feedbackDevice = FeedbackDevice.QuadEncoder;
		}

		boolean inverted = Config.getBoolean("swerve_inverted") ^ Config.getBoolean(name + "_inverted");
		rotateMotor.setInverted(inverted);
		rotateMotor.setSensorPhase(inverted);

		rotateMotor.setNeutralMode(NeutralMode.Brake);
		driveMotor.setNeutralMode(NeutralMode.Brake);
		rotateMotor.configSelectedFeedbackSensor(feedbackDevice, 0, 0);
		driveMotor.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 0);
		rotateMotor.config_kP(0, kP / TICKS_PER_ROTATION, 0);
		rotateMotor.config_kI(0, kI / TICKS_PER_ROTATION, 0);
		rotateMotor.config_kD(0, kD / TICKS_PER_ROTATION, 0);
		rotateMotor.configMaxIntegralAccumulator(0, maxIAccum, 0);
		rotateMotor.configAllowableClosedloopError(0, 0, 0);

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
		SmartDashboard.putNumber(name + " Enc Pos", rotateMotor.getSelectedSensorPosition(0));
		double targetPosition = radians / TWO_PI;
		targetPosition = ((targetPosition % 1.0) + 1.0) % 1.0;

		int encoderPosition = rotateMotor.getSelectedSensorPosition(0) - OFFSET;
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
			double encoderPos = targetPosition * TICKS_PER_ROTATION + OFFSET;
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

	public double getDriveSpeed() {
		return driveMotor.getSelectedSensorVelocity(0) * DRIVE_TICKS_TO_MPS;
	}

	public double getCurrentPosition() {
		return (((rotateMotor.getSelectedSensorPosition(0) * TWO_PI / TICKS_PER_ROTATION) % TWO_PI) + TWO_PI) % TWO_PI;
	}

}
