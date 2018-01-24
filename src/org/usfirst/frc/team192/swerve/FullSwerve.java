package org.usfirst.frc.team192.swerve;

import org.usfirst.frc.team192.robot.JoystickInput;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class FullSwerve extends SwerveBase {
	protected ADXRS450_Gyro gyro;
	private final double MAX_JOYSTICK_VALUE = Math.sqrt(2);
	private final double MAX_ROTATE_VALUE = 1;
	private double ROTATE_SCALE;

	private final double RADIUS;

	public FullSwerve(ADXRS450_Gyro gyro) {
		super();
		this.gyro = gyro;
		RADIUS = Math.sqrt(robotWidth * robotWidth + robotHeight * robotHeight) / 2;
		ROTATE_SCALE = (1 - SPEED_SCALE * MAX_JOYSTICK_VALUE) / (MAX_ROTATE_VALUE * RADIUS);
	}

	@Override
	public void zero() {
		System.out.println("Zeroing gyro - DO NOT MOVE ROBOT");
		for (Wheel wheel : wheels)
			wheel.disable();
		gyro.calibrate();
		gyro.reset();
		super.zero();
	}

	protected void changeMotors(double rv, double vx, double vy) {
		double currentAngle = Math.toRadians(gyro.getAngle());
		SmartDashboard.putNumber("gyro", currentAngle);
		rv *= ROTATE_SCALE * -1;
		vx *= SPEED_SCALE;
		vy *= SPEED_SCALE;
		SmartDashboard.putNumber("rv", rv);
		SmartDashboard.putNumber("vx", vx);
		SmartDashboard.putNumber("vy", vy);
		for (int i = 0; i < 4; i++) {
			double wheelAngle = Math.atan2(robotWidth, robotHeight);
			if (i == 0 || i == 3) {
				wheelAngle *= -1;
			}
			if (i == 2 || i == 3) {
				wheelAngle += Math.PI;
			}
			wheelAngle += currentAngle;
			double dx = RADIUS * Math.cos(wheelAngle);
			double dy = RADIUS * Math.sin(wheelAngle);
			double actualvx = vx + rv * dy;
			double actualvy = vy - rv * dx;
			double wheelTheta = Math.atan2(actualvy, actualvx);
			double speed = Math.sqrt(actualvx * actualvx + actualvy * actualvy);
			double targetPosition = wheelTheta - currentAngle;
			if (speed > 0.1) {
				wheels[i].setDriveSpeed(speed);
				SmartDashboard.putNumber("drive speed " + i, speed);
				wheels[i].setTargetPosition(targetPosition);
				SmartDashboard.putNumber("target position " + i, targetPosition);
			} else {
				wheels[i].setDriveSpeed(0);
				SmartDashboard.putNumber("drive speed " + i, 0);
			}
		}
	}

	@Override
	public void updateTeleop(JoystickInput input) {
		XboxController xbox = input.getXboxController();
		if (xbox.getAButton() && xbox.getYButton())
			zero();
		changeMotors(input.getClippedX(Hand.kRight), -input.getClippedY(Hand.kLeft), input.getClippedX(Hand.kLeft));
	}

}
