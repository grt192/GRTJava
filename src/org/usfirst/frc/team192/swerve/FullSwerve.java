package org.usfirst.frc.team192.swerve;

import org.usfirst.frc.team192.robot.JoystickInput;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class FullSwerve extends SwerveBase {
	private ADXRS450_Gyro gyro;
	private final double MAX_JOYSTICK_VALUE = Math.sqrt(2);
	private final double MAX_ROTATE_VALUE = 1;
	private double ROTATE_SCALE;
	private Mode mode;
	
	// for zero mode
	private int wheelIndex;
	private boolean pressingAButton;
	
	private enum Mode {
		SWERVE, ZERO
	}

	public FullSwerve(double robotWidth, double robotHeight, ADXRS450_Gyro gyro) {
		super(robotWidth, robotHeight, true);
		this.gyro = gyro;
		double r = Math.sqrt(robotWidth * robotWidth + robotHeight * robotHeight);
		ROTATE_SCALE = (1 - SPEED_SCALE * MAX_JOYSTICK_VALUE) / (MAX_ROTATE_VALUE * r);
		this.mode = Mode.SWERVE;
	}

	@Override
	public void zero() {
		gyro.calibrate();
		gyro.reset();
	}

	protected void changeMotors(double rv, double vx, double vy) {
		double currentAngle = Math.toRadians(gyro.getAngle());
		rv *= ROTATE_SCALE * -1;
		vx *= SPEED_SCALE;
		vy *= SPEED_SCALE;
		SmartDashboard.putNumber("rv", rv);
		SmartDashboard.putNumber("vx", vx);
		SmartDashboard.putNumber("vy", vy);
		double r = Math.sqrt(robotWidth * robotWidth + robotHeight * robotHeight) / 2;
		double maxDriveSpeed = 0;
		for (int i = 0; i < 4; i++) {
			double wheelAngle = Math.atan2(robotWidth, robotHeight);
			if (i == 0 || i == 3) {
				wheelAngle *= -1;
			}
			if (i == 2 || i == 3) {
				wheelAngle += Math.PI;
			}
			wheelAngle += currentAngle;
			double dx = r * Math.cos(wheelAngle);
			double dy = r * Math.sin(wheelAngle);
			double actualvx = vx + rv * dy;
			double actualvy = vy - rv * dx;
			double wheelTheta = Math.atan2(actualvy, actualvx);
			double speed = Math.sqrt(actualvx * actualvx + actualvy * actualvy);
			maxDriveSpeed = Math.max(maxDriveSpeed, Math.abs(speed));
			double targetPosition = wheelTheta - currentAngle;
			if (speed > 0.1) {
				wheels[i].setDriveSpeed(speed);
				SmartDashboard.putNumber("drive speed " + i, speed);
				wheels[i].setTargetPosition(targetPosition);
				SmartDashboard.putNumber("target position " + i, targetPosition);
			} else {
				wheels[i].setDriveSpeed(0);
				SmartDashboard.putNumber("speed " + i, 0);
			}
			SmartDashboard.putNumber("gyro", currentAngle);
		}
	}
	
	private void changeMode(JoystickInput input) {
		XboxController xbox = input.getXboxController();
		if (xbox.getAButton() && xbox.getYButton()) {
			mode = Mode.ZERO;
			wheelIndex = 0;
			for (Wheel wheel : wheels) {
				wheel.disable();
			}
		}
	}

	private void performSwerve(JoystickInput input) {
		changeMotors(input.getClippedX(Hand.kRight), -input.getClippedY(Hand.kLeft), input.getClippedX(Hand.kLeft));
	}
	
	@Override
	public void update(JoystickInput input) {
		changeMode(input);
		if (mode == Mode.SWERVE) {
			performSwerve(input);
		} else if (mode == Mode.ZERO) {
			if (input.getXboxController().getAButton() && !pressingAButton) {
				if (wheelIndex == 3) {
					mode = Mode.SWERVE;
					zero();
				} else {
					wheels[wheelIndex++].zero();
				}
				pressingAButton = true;
			} else {
				rotates[wheelIndex].set(ControlMode.PercentOutput, input.getClippedX(Hand.kLeft));
			}
		} else {
			System.out.println("unidentified mode");
		}
	}

}





