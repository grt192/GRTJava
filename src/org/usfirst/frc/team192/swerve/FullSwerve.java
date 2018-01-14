package org.usfirst.frc.team192.swerve;

import org.usfirst.frc.team192.robot.JoystickInput;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.GenericHID.Hand;
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
			double wheelAngle = Math.atan2(robotWidth, -robotHeight); // should be (robotWidth, robotHeight) ?? because
																		// x axis and y axis are swapped but not
																		// reversed
			if (i == 0 || i == 3) {
				wheelAngle *= -1;
			}
			if (i == 2 || i == 3) {
				wheelAngle += Math.PI;
			}
			wheelAngle -= currentAngle; // I think you need to add the gyro angle
			double dx = r * Math.cos(Math.PI / 2 - wheelAngle); // I think it just has to be cos(wheelAngle) because the
																// axes are already swapped
			double dy = r * Math.sin(Math.PI / 2 - wheelAngle); // see above
			double actualvx = vx + rv * dy; // probably multiply rv by a scalar so it has less influence
			double actualvy = vy - rv * dx;
			double wheelTheta = realAtan(actualvx, actualvy); // realAtan() also inverts y. I don't think this is
																// necessary
			double speed = Math.sqrt(actualvx * actualvx + actualvy * actualvy); // at least this part makes sense :)
			wheels[i].setDriveSpeed(speed * DRIVE_RATIO); // we also need to normalize it do it is never >1
			wheels[i].setTargetPosition(wheelTheta); // don't you need to subtract the gyro angle?
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
