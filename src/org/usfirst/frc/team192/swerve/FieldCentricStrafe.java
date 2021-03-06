package org.usfirst.frc.team192.swerve;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.GyroBase;
import edu.wpi.first.wpilibj.XboxController;

public class FieldCentricStrafe extends Strafe {

	private final double TWO_PI = 2 * Math.PI;

	private GyroBase gyro;

	public FieldCentricStrafe(GyroBase gyro) {
		super();

		this.gyro = gyro;
	}

	@Override
	public void zero() {
		gyro.calibrate();
		gyro.reset();
		super.zero();
	}

	@Override
	public void updateWithJoystick(XboxController input) {
		changeMode(input);
		double gyroAngle = Math.toRadians(gyro.getAngle());
		double x = -input.getY(Hand.kLeft);
		double y = input.getX(Hand.kLeft);
		double angle = Math.atan2(y, x);
		double radius = Math.sqrt(x * x + y * y);
		double speed = 0.0;
		if (currentMode == Mode.STRAFE) {
			angle -= gyroAngle;
			speed = radius * SPEED_SCALE;
			for (Wheel wheel : wheels) {
				if (wheel == null)
					continue;
				if (radius > 0.2) {
					wheel.setTargetPosition(angle - gyroAngle);
					wheel.setDriveSpeed(speed);
				} else
					wheel.setDriveSpeed(0.0);
			}
		} else if (currentMode == Mode.ROTATE) {
			if (radius < 0.3)
				speed = 0.0;
			else {
				speed = -1.0;
				gyroAngle = ((gyroAngle % TWO_PI) + TWO_PI) % TWO_PI;
				if (angle < -Math.PI)
					angle += TWO_PI;
				double delta = gyroAngle - angle;
				if (delta < 0)
					delta += TWO_PI;
				if (delta > Math.PI) {
					speed = 1.0;
					delta = TWO_PI - delta;
				}
				speed *= delta * SPEED_SCALE / Math.PI;

			}
			for (Wheel wheel : wheels)
				wheel.setDriveSpeed(speed);

		} else {
			System.out.println("something has gone horribly wrong");
		}
	}

}
