package org.usfirst.frc.team192.swerve;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.XboxController;

public class Strafe extends SwerveBase {

	protected Mode currentMode;

	protected enum Mode {
		STRAFE, ROTATE
	}

	public Strafe() {
		super();

		currentMode = Mode.STRAFE;

	}

	protected void changeMode(XboxController input) {
		Mode lastMode = currentMode;
		if (input.getXButton() && input.getYButton() && currentMode == Mode.STRAFE)
			zero();

		if (input.getAButtonPressed()) {
			currentMode = Mode.STRAFE;
		} else if (input.getBButtonPressed()) {
			currentMode = Mode.ROTATE;
		}

		if (lastMode != currentMode && currentMode == Mode.ROTATE) {
			double robotAngle = Math.atan2(robotHeight, robotWidth);
			wheels[0].setTargetPosition(robotAngle);
			wheels[1].setTargetPosition(Math.PI - robotAngle);
			wheels[2].setTargetPosition(-robotAngle);
			wheels[3].setTargetPosition(robotAngle + Math.PI);
		}
	}

	@Override
	public void updateWithJoystick(XboxController input) {
		changeMode(input);
		if (currentMode == Mode.STRAFE) {
			double x = -input.getY(Hand.kLeft);
			double y = input.getX(Hand.kLeft);
			double angle = Math.atan2(y, x);
			double speed = Math.sqrt(x * x + y * y);
			speed *= SPEED_SCALE;
			for (Wheel wheel : wheels) {
				if (wheel == null)
					continue;
				if (speed > 0.2)
					wheel.setTargetPosition(angle);
				if (speed < 0.2)
					wheel.setDriveSpeed(0.0);
				else
					wheel.setDriveSpeed(speed);
			}
		} else if (currentMode == Mode.ROTATE) {
			double speed = input.getX(Hand.kLeft);
			speed *= SPEED_SCALE;
			for (Wheel wheel : wheels) {
				wheel.setDriveSpeed(speed);
			}
		} else {
			System.out.println("something has gone horribly wrong");
		}
	}

}
