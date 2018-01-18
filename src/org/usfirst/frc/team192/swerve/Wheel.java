package org.usfirst.frc.team192.swerve;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Timer;

class Wheel extends Thread {

	private final int TICKS_PER_ROTATION = 8192;
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

	private static final FeedbackDevice defaultSensor = FeedbackDevice.QuadEncoder;
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
		zero();
	}

	public void enable() {
		running = true;
		rotateMotor.set(ControlMode.Disabled, 0);
		setDriveSpeed(0);
		targetAngle = rotateMotor.getSelectedSensorPosition(0);
		driveSpeed = 0;
		if (useLimitSwitch)
			start();
	}

	public void zero() {
		if (useLimitSwitch)
			offset = (double) rotateMotor.getSelectedSensorPosition(0) / TICKS_PER_ROTATION;
		else
			setSelectedSensorPosition(0);
		setTargetPosition(0);
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

	@Override
	public void run() {
		boolean switchValue = false;
		switchValue = limitSwitch.get();
		while (running) {
			boolean newSwitchValue = limitSwitch.get();
			if (newSwitchValue != switchValue) {
				boolean forward = (Math.signum(rotateMotor.getSelectedSensorVelocity(0)) == 1.0f);
				if (forward != switchValue) {
					setSelectedSensorPosition(0);
				}
				switchValue = newSwitchValue;
			}
			Timer.delay(LIMIT_SWITCH_READ_DELAY);
		}
	}

	public void setTargetPosition(double radians) {
		double targetPosition = radians / TWO_PI;
		if (useLimitSwitch)
			targetPosition += offset;
		// System.out.println(rotateMotor.getSelectedSensorPosition(0) /
		// TICKS_PER_ROTATION + ", " + targetPosition);
		targetPosition = ((targetPosition % 1.0) + 1.0) % 1.0;
		if (targetPosition > 0.5)
			targetPosition--;

		double currentPosition = (double) (rotateMotor.getSelectedSensorPosition(0) % TICKS_PER_ROTATION)
				/ TICKS_PER_ROTATION;
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
			// System.out.println("changing " + Math.abs(targetPosition - targetAngle));
			if (positionChanged)
				setSelectedSensorPosition((int) (currentPosition * TICKS_PER_ROTATION));
			reversed = newReverse;
			targetAngle = targetPosition;
			double encoderPos = (targetPosition) * TICKS_PER_ROTATION;
			// System.out.println("going to " + encoderPos);
			rotateMotor.set(ControlMode.Position, encoderPos);
		}

	}

	private void setSelectedSensorPosition(int pos) {
		if (sensor == FeedbackDevice.PulseWidthEncodedPosition)
			rotateMotor.getSensorCollection().setPulseWidthPosition(pos, 0);
		else if (sensor == FeedbackDevice.QuadEncoder)
			rotateMotor.getSensorCollection().setQuadraturePosition(pos, 0);
	}

	public void setDriveSpeed(double speed) {
		speed *= (reversed ? -1 : 1);
		if ((speed == 0.0 && driveSpeed != 0.0) || Math.abs(driveSpeed - speed) > MIN_SPEED_CHANGE) {
			driveMotor.set(ControlMode.PercentOutput, speed);
			driveSpeed = speed;
		}
	}

}
