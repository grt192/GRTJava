package org.usfirst.frc.team192.swerve;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.XboxController;

public class TankSwerve extends SwerveBase {

	public TankSwerve() {
		super();
		for (Wheel wheel : wheels)
			wheel.setTargetPosition(0.0);
	}

	@Override
	public void updateWithJoystick(XboxController input) {
		double xSpeed = -input.getY(Hand.kLeft);
		double zRotation = input.getX(Hand.kLeft);

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

		wheels[0].setDriveSpeed(leftMotorOutput);
		wheels[2].setDriveSpeed(leftMotorOutput);
		wheels[1].setDriveSpeed(rightMotorOutput);
		wheels[3].setDriveSpeed(rightMotorOutput);
	}

}
