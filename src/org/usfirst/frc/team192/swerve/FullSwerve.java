package org.usfirst.frc.team192.swerve;

import org.usfirst.frc.team192.robot.JoystickInput;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.XboxController;

public class FullSwerve extends SwerveBase {
	private ADXRS450_Gyro gyro;

	public FullSwerve(double robotWidth, double robotHeight, ADXRS450_Gyro gyro) {
		super(robotWidth, robotHeight);
		this.gyro = gyro;
	}

	private double calcrv(JoystickInput input) {
		return input.getClippedX(Hand.kRight);
	}

	private double calcvx(JoystickInput input) {
		return -input.getClippedY(Hand.kLeft);
	}

	private double calcvy(JoystickInput input) {
		return input.getClippedX(Hand.kLeft);
	}
	
	@Override
	public void zero() {
		gyro.calibrate();
		gyro.reset();
		super.zero();
	}

	private void changeMotors(double rv, double vx, double vy) {
		double currentAngle = Math.toRadians(gyro.getAngle());
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
			}
			double driveRatio = Math.min(1, 1 / maxDriveSpeed); // should only scale down if the wheels should go really fast
			for (int i = 0; i < 4; i++) {
				wheels[i].setDriveSpeed(driveSpeeds[i] * driveRatio);
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
		changeMotors(calcrv(input), calcvx(input), calcvy(input));
	}

}
