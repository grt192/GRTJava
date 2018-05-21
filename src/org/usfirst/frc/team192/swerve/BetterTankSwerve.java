package org.usfirst.frc.team192.swerve;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.XboxController;

public class BetterTankSwerve extends SwerveBase {

	@Override
	public void updateWithJoystick(XboxController input) {
		double xSpeed = -input.getY(Hand.kLeft);
		double zRotation = input.getX(Hand.kLeft);
		drive(xSpeed, zRotation);
	}

	public void drive(double xSpeed, double zRotation) {
		xSpeed = Math.copySign(xSpeed * xSpeed, xSpeed);
		zRotation = Math.copySign(zRotation * zRotation, zRotation);

		double leftMotorOutput;
		double rightMotorOutput;

		double maxInput = Math.copySign(Math.max(Math.abs(xSpeed), Math.abs(zRotation)), xSpeed);

		if (xSpeed >= 0.0) {
			// First quadrant, else second quadrant
			if (zRotation >= 0.0) {
				leftMotorOutput = maxInput;
				rightMotorOutput = xSpeed - zRotation;
			} else {
				leftMotorOutput = xSpeed + zRotation;
				rightMotorOutput = maxInput;
			}
		} else {
			// Third quadrant, else fourth quadrant
			if (zRotation >= 0.0) {
				leftMotorOutput = xSpeed + zRotation;
				rightMotorOutput = maxInput;
			} else {
				leftMotorOutput = maxInput;
				rightMotorOutput = xSpeed - zRotation;
			}
		}

		if (leftMotorOutput == 0.0 && rightMotorOutput == 0.0)
			for (Wheel wheel : wheels)
				wheel.setDriveSpeed(0.0);

		double ratio = rightMotorOutput / leftMotorOutput;

		if (ratio > 0 && Math.max(ratio, 1.0 / ratio) < 1.00001) {
			for (Wheel wheel : wheels)
				wheel.setTargetPosition(0.0);
			wheels[0].setDriveSpeed(leftMotorOutput);
			wheels[2].setDriveSpeed(leftMotorOutput);
			wheels[1].setDriveSpeed(rightMotorOutput);
			wheels[3].setDriveSpeed(rightMotorOutput);
		} else {
			double yCenter = (ROBOT_WIDTH / 2) * ((ratio + 1) / (1 - ratio));
			double leftAngle = Math.atan2(ROBOT_HEIGHT / 2, yCenter + ROBOT_WIDTH / 2);
			wheels[0].setTargetPosition(leftAngle);
			wheels[0].setDriveSpeed(leftMotorOutput);
			wheels[2].setTargetPosition(-leftAngle);
			wheels[2].setDriveSpeed(leftMotorOutput);

			double rightAngle = Math.atan2(ROBOT_HEIGHT / 2, yCenter - ROBOT_WIDTH / 2);
			wheels[0].setTargetPosition(rightAngle);
			wheels[0].setDriveSpeed(rightMotorOutput);
			wheels[2].setTargetPosition(-rightAngle);
			wheels[2].setDriveSpeed(rightMotorOutput);
		}
	}

}
