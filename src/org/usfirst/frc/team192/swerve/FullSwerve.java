package org.usfirst.frc.team192.swerve;

import org.usfirst.frc.team192.robot.JoystickInput;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.GyroBase;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class FullSwerve extends SwerveBase {

	private GyroBase gyro;
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

	private final double RADIUS;
	private final double WHEEL_ANGLE;

	public FullSwerve(GyroBase gyro) {
		super();
		this.gyro = gyro;
		this.mode = Mode.SWERVE;
		RADIUS = Math.sqrt(robotWidth * robotWidth + robotHeight * robotHeight) / 2;
		WHEEL_ANGLE = Math.atan2(robotWidth, robotHeight);
		ROTATE_SCALE = (1 - SPEED_SCALE * MAX_JOYSTICK_VALUE) / (MAX_ROTATE_VALUE * RADIUS);
		zero();
	}

	@Override
	public void zero() {
		gyro.calibrate();
		gyro.reset();
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
			double wheelAngle = getRelativeWheelAngle(i);
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
	
	protected void performSwerve(JoystickInput input) {
		changeMotors(input.getClippedX(Hand.kRight), -input.getClippedY(Hand.kLeft), input.getClippedX(Hand.kLeft));
	}
	
	public void updateWithJoystick(JoystickInput input) {
		changeMode(input);
		if (mode == Mode.SWERVE) {
			performSwerve(input);
		} else if (mode == Mode.ZERO) {
			if (input.getXboxController().getAButton() && !pressingAButton) {
				wheels[wheelIndex++].zero();
				SmartDashboard.putNumber("wheelIndex", wheelIndex);
				if (wheelIndex == 4) {
					mode = Mode.SWERVE;
					zero();
				}
				pressingAButton = true;
			} else if (!input.getXboxController().getAButton()) {
				wheels[wheelIndex].getRotateMotor().set(ControlMode.PercentOutput, input.getClippedX(Hand.kLeft) / 3);
				pressingAButton = false;
			}
		} else {
			System.out.println("unidentified mode");
		}
	}

	private double getRelativeWheelAngle(int index) {
		double wheelAngle = WHEEL_ANGLE;
		if (index == 0 || index == 3) {
			wheelAngle *= -1;
		}
		if (index == 2 || index == 3) {
			wheelAngle += Math.PI;
		}
		return wheelAngle;
	}

	public SwerveData getSwerveData() {
		double angVel = 0.0;
		double vx = 0.0;
		double vy = 0.0;
		double gyroAngle = Math.toRadians(gyro.getAngle());
		double gyroRate = Math.toRadians(gyro.getRate());
		for (int i = 0; i < 4; i++) {
			double wheelAngle = getRelativeWheelAngle(i);
			double wheelSpeed = wheels[i].getDriveSpeed();
			double wheelPosition = wheels[i].getCurrentPosition();
			angVel += wheelSpeed * Math.sin(wheelPosition - wheelAngle) / RADIUS;
			double absoluteWheelPosition = wheelPosition + gyroAngle;
			vx += Math.cos(absoluteWheelPosition) * wheelSpeed;
			vy += Math.sin(absoluteWheelPosition) * wheelSpeed;
		}
		// divide by 4 to get average
		return new SwerveData(gyroAngle, gyroRate, angVel / 4, vx / 4, vy / 4);
	}

	public double getGyroAngle() {
		return gyro.getAngle();
	}

	public double getGyroRate() {
		return gyro.getRate();
	}

}





