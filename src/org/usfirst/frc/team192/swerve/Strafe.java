package org.usfirst.frc.team192.swerve;

import org.usfirst.frc.team192.robot.JoystickInput;

import edu.wpi.first.wpilibj.Joystick;

public class Strafe extends SwerveBase {

	protected Mode currentMode;

	protected final double SPEED_SCALE = 1.0 / 3;

	protected enum Mode {
		STRAFE, ROTATE
	}

	public Strafe(double robotWidth, double robotHeight) {
		super(robotWidth, robotHeight);

		currentMode = Mode.STRAFE;

	}

	protected void changeMode(JoystickInput input) {
		Joystick joystick = input.getJoystick();
		Mode lastMode = currentMode;
		if (joystick.getRawButton(6) && joystick.getRawButton(11) && currentMode == Mode.STRAFE)
			zero();

		if (joystick.getRawButton(2)) {
			currentMode = Mode.STRAFE;
		} else if (joystick.getRawButton(3)) {
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
	public void update(JoystickInput input) {
		changeMode(input);
		if (currentMode == Mode.STRAFE) {
			double speed = input.getPolarRadius();
			double angle = input.getPolarAngle();
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
			double speed = input.getJoystick().getX();
			speed *= SPEED_SCALE;
			for (Wheel wheel : wheels) {
				wheel.setDriveSpeed(speed);
			}
		} else {
			System.out.println("something has gone horribly wrong");
		}
	}

}
