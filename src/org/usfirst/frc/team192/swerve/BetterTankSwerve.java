package org.usfirst.frc.team192.swerve;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.XboxController;

public class BetterTankSwerve extends SwerveBase {

	public BetterTankSwerve() {
		super();
	}

	@Override
	public void updateWithJoystick(XboxController input) {
		double left = clip(-input.getY(Hand.kLeft));
		double right = clip(-input.getY(Hand.kRight));
		set(left, right);
	}

	public void set(double left, double right) {
		if (left == 0.0 && right == 0.0) {
			for (Wheel wheel : wheels)
				wheel.setDriveSpeed(0.0);
			return;
		}
		double x = (ROBOT_HEIGHT / 2);
		double y = (ROBOT_WIDTH / 2);

		double yCenter = y * ((left + right) / (left - right));
		double leftDist = (yCenter + y);
		double rightDist = (yCenter - y);
		double omega = 1.0;
		if (left == 0.0) {
			omega = right / rightDist;
		} else {
			omega = left / rightDist;
		}
		double leftAngle = Math.atan(x / leftDist);
		double actualLeft = omega * Math.sqrt(x * x + leftDist * leftDist);
		wheels[0].setTargetPosition(leftAngle);
		wheels[0].setDriveSpeed(actualLeft);
		wheels[2].setTargetPosition(-leftAngle);
		wheels[2].setDriveSpeed(actualLeft);

		double rightAngle = Math.atan(x / rightDist);
		double actualRight = omega * Math.sqrt(x * x + rightDist * rightDist);
		wheels[1].setTargetPosition(rightAngle);
		wheels[1].setDriveSpeed(actualRight);
		wheels[3].setTargetPosition(-rightAngle);
		wheels[3].setDriveSpeed(actualRight);

	}

}
