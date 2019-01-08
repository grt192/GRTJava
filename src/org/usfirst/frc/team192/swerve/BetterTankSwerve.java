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
		System.out.println(yCenter);
		double leftDist = (yCenter + y);
		double rightDist = (yCenter - y);

		double leftAngle = Math.atan(x / leftDist);
		double rightAngle = Math.atan(x / rightDist);

		double actualLeft = left;
		double actualRight = right;
		if (Double.isFinite(yCenter)) {
			double omega = 1.0;
			if (left == 0.0) {
				omega = right / rightDist;
			} else {
				omega = left / leftDist;
			}
			omega = Math.abs(omega);
			actualLeft = omega * Math.sqrt(x * x + leftDist * leftDist) * Math.signum(left);
			actualRight = omega * Math.sqrt(x * x + rightDist * rightDist) * Math.signum(right);
		}
		double scale = Math.min(1, 1 / Math.max(Math.abs(actualLeft), Math.abs(actualRight)));
		actualLeft *= scale;
		actualRight *= scale;

		wheels[0].setTargetPosition(leftAngle);
		wheels[0].setDriveSpeed(actualLeft);
		wheels[2].setTargetPosition(-leftAngle);
		wheels[2].setDriveSpeed(actualLeft);

		wheels[1].setTargetPosition(rightAngle);
		wheels[1].setDriveSpeed(actualRight);
		wheels[3].setTargetPosition(-rightAngle);
		wheels[3].setDriveSpeed(actualRight);

	}

}
