package org.usfirst.frc.team192.swerve;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.DigitalInput;

class Wheel {

	private final int TICKS_PER_ROTATION = 8533;
	private final double LIMIT_SWITCH_READ_DELAY = 0.05;
	private final double TWO_PI = Math.PI * 2;

	private final double MIN_ANGLE_CHANGE = 0.005;
	private final double MIN_SPEED_CHANGE = 0.05;

	private TalonSRX rotateMotor;
	private TalonSRX driveMotor;
	private DigitalInput limitSwitch;
	private boolean useLimitSwitch;

	private boolean running;
	public boolean reversed;

	private double targetAngle;
	private double driveSpeed;
	private double offset;

	private FeedbackDevice sensor;

	private static final FeedbackDevice defaultSensor = FeedbackDevice.PulseWidthEncodedPosition;
	private static final boolean defaultUseLimitSwitch = false;

	public Wheel(TalonSRX rotateMotor, TalonSRX driveMotor) {
		this(rotateMotor, driveMotor, null, defaultUseLimitSwitch, defaultSensor);
	}

	public Wheel(TalonSRX rotateMotor, TalonSRX driveMotor, DigitalInput limitSwitch) {
		this(rotateMotor, driveMotor, limitSwitch, defaultUseLimitSwitch, defaultSensor);
	}

	public Wheel(TalonSRX rotateMotor, TalonSRX driveMotor, DigitalInput limitSwitch, boolean useLimitSwitch,
			FeedbackDevice sensor) {
		this.rotateMotor = rotateMotor;
		this.driveMotor = driveMotor;
		this.limitSwitch = limitSwitch;

		this.sensor = sensor;

		this.useLimitSwitch = (limitSwitch != null) && useLimitSwitch;
	}

	public void initialize() {
		rotateMotor.setNeutralMode(NeutralMode.Brake);
		driveMotor.setNeutralMode(NeutralMode.Brake);
		rotateMotor.configSelectedFeedbackSensor(sensor, 0, 0);
		rotateMotor.config_kP(0, 1.0, 0);
		rotateMotor.config_kI(0, 0.0, 0);
		rotateMotor.config_kD(0, 0.0, 0);

		rotateMotor.config_kP(1, 0.0, 0);
		rotateMotor.config_kI(1, 0.0, 0);
		rotateMotor.config_kD(1, 0.0, 0);
		zero();
	}

	public void enable() {
		running = true;
		rotateMotor.set(ControlMode.Disabled, 0);
		setDriveSpeed(0);
		targetAngle = rotateMotor.getSelectedSensorPosition(0);
		driveSpeed = 0;
	}

	public void zero() {
		rotateMotor.getSensorCollection().setQuadraturePosition(0, 0);
	}

	public void disable() {
		rotateMotor.set(ControlMode.Disabled, 0);
		driveMotor.set(ControlMode.Disabled, 0);
		running = false;
	}

	public Wheel copy() {
		Wheel wheel = new Wheel(rotateMotor, driveMotor, limitSwitch);
		wheel.offset = offset;
		return wheel;
	}

	public void setTargetPosition(double radians) {
		double targetPosition = radians / TWO_PI;
		if (useLimitSwitch)
			targetPosition += offset;
		targetPosition = ((targetPosition % 1.0) + 1.0) % 1.0;
		if (targetPosition > 0.5)
			targetPosition--;

		int encoderPosition = rotateMotor.getSelectedSensorPosition(0);
		double currentPosition = encoderPosition;
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
		if (Math.abs(targetPosition - targetAngle) > MIN_ANGLE_CHANGE || newReverse != reversed) {
			reversed = newReverse;
			targetAngle = targetPosition;
			double encoderPos = (targetPosition) * TICKS_PER_ROTATION;
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
