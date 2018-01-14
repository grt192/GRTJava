package org.usfirst.frc.team192.swerve;

import org.usfirst.frc.team192.robot.JoystickInput;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.Joystick;

public class FullSwerve extends SwerveBase {
	private ADXRS450_Gyro gyro;
	
	private final double DRIVE_RATIO = 1.0 / 2;

	public FullSwerve(double robotWidth, double robotHeight, ADXRS450_Gyro gyro) {
		super(robotWidth, robotHeight);
		this.gyro = gyro;
	}

	private double calcrv(JoystickInput input) {
		return input.getClippedX(Hand.kLeft);
	}

	private double calcvx(JoystickInput input) {
		return -input.getClippedY(Hand.kLeft);
	}

	private double calcvy(JoystickInput input) {
		return input.getClippedX(Hand.kRight);
	}

	private void changeMotors(double rv, double vx, double vy) {
		double currentAngle = Math.toRadians(gyro.getAngle());
		for (int i = 0; i < 4; i++) {
			double r = Math.sqrt(robotWidth * robotWidth + robotHeight * robotHeight) / 2;
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
			double wheelTheta = Math.atan2(actualvy, actualvx); // haha
			double speed = Math.sqrt(actualvx * actualvx + actualvy * actualvy);
			wheels[i].setDriveSpeed(speed * DRIVE_RATIO);
			wheels[i].setTargetPosition(wheelTheta - currentAngle);
		}
	}

	@Override
	public void update(JoystickInput input) {
		Joystick joystick = input.getJoystick();
		if (joystick.getRawButton(6) && joystick.getRawButton(11))
			for (Wheel wheel : wheels)
				if (wheel != null)
					wheel.zero();
		changeMotors(calcrv(input), calcvx(input), calcvy(input));
	}

}
