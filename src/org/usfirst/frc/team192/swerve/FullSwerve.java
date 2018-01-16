package org.usfirst.frc.team192.swerve;

import org.usfirst.frc.team192.robot.JoystickInput;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.XboxController;

public class FullSwerve extends SwerveBase {
	private ADXRS450_Gyro gyro;
	private long lastUpdated;
	
	// for PID loop
	private double integral;
	private double p;
	private double i;
	private double d;

	public FullSwerve(double robotWidth, double robotHeight, ADXRS450_Gyro gyro) {
		super(robotWidth, robotHeight);
		this.gyro = gyro;
		this.lastUpdated = System.currentTimeMillis();
		this.integral = 0;
		this.p = 1.0;
		this.i = 1.0;
		this.d = 1.0;
	}
	
	@Override
	public void zero() {
		gyro.calibrate();
		gyro.reset();
		super.zero();
	}

	private void changeMotors(double rv, double vx, double vy) {
		double currentAngle = Math.toRadians(gyro.getAngle());
		SmartDashboard.putNumber("rv", rv);
		if (Math.sqrt(vx * vx + vy * vy + rv * rv) > 0.3) {
			double r = Math.sqrt(robotWidth * robotWidth + robotHeight * robotHeight) / 2;
			double[] driveSpeeds = new double[4];
			double maxDriveSpeed = 0;
			for (int i = 0; i < 4; i++) {
				double wheelAngle = Math.atan2(robotWidth, robotHeight);
				if (i == 0 || i == 3) {
					wheelAngle *= -1;
				}
				if (i == 2 || i == 3) {
					wheelAngle += Math.PI;
				}
				wheelAngle += currentAngle;
				double dx = r * Math.cos(wheelAngle);
				double dy = r * Math.sin(wheelAngle);
				double actualvx = vx + rv * dy;
				double actualvy = vy - rv * dx;
				double wheelTheta = Math.atan2(actualvy, actualvx);
				double speed = Math.sqrt(actualvx * actualvx + actualvy * actualvy);
				driveSpeeds[i] = speed;
				maxDriveSpeed = Math.max(maxDriveSpeed, Math.abs(speed));
				wheels[i].setTargetPosition(wheelTheta - currentAngle);
				SmartDashboard.putNumber("target position " + i, wheelTheta - currentAngle);
			}
			double driveRatio = Math.min(1, 1 / maxDriveSpeed) / 3; // should only scale down if the wheels should go really fast
			for (int i = 0; i < 4; i++) {
				wheels[i].setDriveSpeed(driveSpeeds[i] * driveRatio);
				SmartDashboard.putNumber("drive speed " + i, driveSpeeds[i] * driveRatio);
			}
		} else {
			for (Wheel wheel : wheels) {
				wheel.setDriveSpeed(0);
			}
		}
		System.out.println(currentAngle);
	}

	@Override
	public void update(JoystickInput input) {
		XboxController xbox = input.getXboxController();
		if (xbox.getAButton() && xbox.getYButton())
			zero();
		double targetAngle = (Math.atan2(input.getClippedX(Hand.kRight), input.getClippedY(Hand.kRight)) + 2 * Math.PI) % (2 * Math.PI);
		double currentAngle = ((Math.toRadians(gyro.getAngle()) % 2 * Math.PI) + 2 * Math.PI) % 2 * Math.PI;
		double error = targetAngle - currentAngle;
		if (Math.abs(error) > Math.PI) {
			error -= Math.PI * 2 * Math.signum(error);
		}
		long newTime = System.currentTimeMillis();
		double dt = (newTime - lastUpdated) / 1000.0;
		lastUpdated = newTime;
		integral += dt * error;
		double rv = (error * p + Math.toRadians(gyro.getRate()) * d + integral * i) / Math.PI;
		changeMotors(-input.getClippedX(Hand.kRight), -input.getClippedY(Hand.kLeft), -rv);
	}

}
