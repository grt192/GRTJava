package org.usfirst.frc.team192.swerve;

import org.usfirst.frc.team192.robot.JoystickInput;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.Joystick;

public class FullSwerve extends SwerveBase {

	public FullSwerve(double robotWidth, double robotHeight) {
		super(robotWidth, robotHeight);
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
		for (int i = 0; i < 4; i++) {
			double dx = robotWidth / 2 * (((i % 2) * 2) - 1);
			double dy = robotHeight / 2 * (((i / 2) * 2) - 1);
			double actualvx = vx + rv * dy;
			double actualvy = vy - rv * dx;
			double wheelTheta = realAtan(actualvx, -actualvy); // haha
			double speed = Math.sqrt(actualvx * actualvx + actualvy * actualvy);
			wheels[i].setDriveSpeed(speed / 2);
			wheels[i].setTargetPosition(wheelTheta);
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
