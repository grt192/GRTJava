package org.usfirst.frc.team192.swerve;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.interfaces.Gyro;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class FullSwerve extends SwerveBase {

	private Gyro gyro;

	private final double RADIUS;
	private final double WHEEL_ANGLE;
	private final double ROTATE_SCALE;

	public FullSwerve(Gyro gyro) {
		super();
		this.gyro = gyro;
		RADIUS = Math.sqrt(robotWidth * robotWidth + robotHeight * robotHeight) / 2;
		WHEEL_ANGLE = Math.atan2(robotWidth, robotHeight);
		ROTATE_SCALE = 0.5 / RADIUS;
		zero();
	}

	@Override
	public void zero() {
		for (Wheel wheel : wheels)
			wheel.disable();
		zeroGyro();
		super.zero();
	}

	public void zeroGyro() {
		System.out.println("Zeroing gyro - DO NOT MOVE ROBOT");
		gyro.calibrate();
		System.out.println("Done zeroing gyro");
	}

	protected void changeMotors(double rv, double vx, double vy) {
		double currentAngle = Math.toRadians(gyro.getAngle());
		SmartDashboard.putNumber("gyro", currentAngle);
		rv *= -1 * ROTATE_SCALE;
		SmartDashboard.putNumber("rv", rv);
		SmartDashboard.putNumber("vx", vx);
		SmartDashboard.putNumber("vy", vy);
		double[] wheelSpeeds = new double[4];
		double[] wheelAngles = new double[4];
		double maxSpeed = 0;
		for (int i = 0; i < 4; i++) {
			double wheelAngle = getRelativeWheelAngle(i);
			wheelAngle += currentAngle;
			double dx = RADIUS * Math.cos(wheelAngle);
			double dy = RADIUS * Math.sin(wheelAngle);
			double actualvx = vx + rv * dy;
			double actualvy = vy - rv * dx;
			double wheelTheta = Math.atan2(actualvy, actualvx);
			double speed = Math.sqrt(actualvx * actualvx + actualvy * actualvy);
			double targetPosition = wheelTheta - currentAngle;
			wheelSpeeds[i] = speed;
			wheelAngles[i] = targetPosition;
			maxSpeed = Math.max(maxSpeed, speed);
		}
		if (maxSpeed > 0.1) {
			double scale = 1 / Math.max(1, maxSpeed);
			for (int i = 0; i < 4; i++) {
				wheels[i].setDriveSpeed(scale * wheelSpeeds[i]);
				SmartDashboard.putNumber("drive speed " + i, scale * wheelSpeeds[i]);
				wheels[i].setTargetPosition(wheelAngles[i]);
				SmartDashboard.putNumber("wheel angle  " + i, wheelAngles[i]);
			}
		} else {
			for (int i = 0; i < 4; i++) {
				wheels[i].setDriveSpeed(0);
				SmartDashboard.putNumber("drive speed " + i, 0);
			}
		}
	}

	@Override
	public void updateWithJoystick(XboxController input) {
		if (input.getAButtonPressed())
			zeroGyro();
		changeMotors(input.getX(Hand.kRight), -input.getY(Hand.kLeft), input.getX(Hand.kLeft));
	}

	private double getRelativeWheelAngle(int index) {
		double wheelAngle = WHEEL_ANGLE;
		if (index == 0 || index == 3) {
			wheelAngle *= -1;
		}
		if (index == 2 || index == 3) {
			wheelAngle += Math.PI;
		}
		return wheelAngle;
	}

	public SwerveData getSwerveData() {
		double angVel = 0.0;
		double vx = 0.0;
		double vy = 0.0;
		double gyroAngle = Math.toRadians(gyro.getAngle());
		double gyroRate = Math.toRadians(gyro.getRate());
		for (int i = 0; i < 4; i++) {
			double wheelAngle = getRelativeWheelAngle(i);
			double wheelSpeed = wheels[i].getDriveSpeed();
			double wheelPosition = wheels[i].getCurrentPosition();
			angVel += wheelSpeed * Math.sin(wheelPosition - wheelAngle) / RADIUS;
			double absoluteWheelPosition = wheelPosition + gyroAngle;
			vx += Math.cos(absoluteWheelPosition) * wheelSpeed;
			vy += Math.sin(absoluteWheelPosition) * wheelSpeed;
		}
		// divide by 4 to get average
		return new SwerveData(gyroAngle, gyroRate, angVel / 4, vx / 4, vy / 4);
	}

	public double getGyroAngle() {
		return gyro.getAngle();
	}

	public double getGyroRate() {
		return gyro.getRate();
	}

}
