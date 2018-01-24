package org.usfirst.frc.team192.swerve;

import org.usfirst.frc.team192.robot.JoystickInput;

public class TankSwerve extends SwerveBase {

	public TankSwerve(double robotWidth, double robotHeight) {
		super(robotWidth, robotHeight, true);
	}

	@Override
	public void updateTeleop(JoystickInput input) {
		double xSpeed = -input.getTurnStickY();
		double zRotation = input.getTurnStickX();

		// Square the inputs (while preserving the sign) to increase fine control
		// while permitting full power.
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

		leftMotorOutput /= 2;
		rightMotorOutput /= 2;

		wheels[0].setDriveSpeed(leftMotorOutput);
		wheels[2].setDriveSpeed(leftMotorOutput);
		wheels[1].setDriveSpeed(rightMotorOutput);
		wheels[3].setDriveSpeed(rightMotorOutput);
	}

}
