package org.usfirst.frc.team192.strafe;

import org.usfirst.frc.team192.robot.JoystickInput;
import org.usfirst.frc.team192.swerve.WheelDriveThread;
import org.usfirst.frc.team192.swerve.WheelRotateThread;

public class Strafe {
	/*
	private WheelDriveThread frontRightDrive;
	private WheelRotateThread frontRightRotate;
	private WheelDriveThread frontLeftDrive;
	private WheelRotateThread frontLeftRotate;
	private WheelDriveThread backRightDrive;
	private WheelRotateThread backRightRotate;
	private WheelDriveThread backLeftDrive;
	private WheelRotateThread backLeftRotate;
	*/
	
	private WheelDriveThread[] drives;
	private WheelRotateThread rotates;
	private double DRIVE_SCALE;
	private double robotWidth;
	private double robotHeight;
	
	private enum DriveMode {
		STRAFE, ROTATE
	}
	
	private DriveMode mode;
	
	public Strafe(WheelRotateThread wheelRotate, WheelDriveThread frontRightDrive, WheelDriveThread frontLeftDrive, WheelDriveThread backRightDrive, WheelDriveThread backLeftDrive, double robotWidth, double robotHeight) {
		/*
		this.frontRightDrive = frontRightDrive;
		this.frontRightRotate = frontRightRotate;
		this.frontLeftDrive = frontRightDrive;
		this.frontLeftRotate = frontRightRotate;
		this.backRightDrive = frontRightDrive;
		this.backRightRotate = frontRightRotate;
		this.backLeftDrive = frontRightDrive;
		this.backLeftRotate = frontRightRotate;
		*/
		
		drives = new WheelDriveThread[4];
		drives[0] = frontRightDrive;
		drives[1] = frontLeftDrive;
		drives[2] = backRightDrive;
		drives[3] = backLeftDrive;
		
		rotates = wheelRotate;
		
		DRIVE_SCALE = 1 / Math.sqrt(2);
		mode = DriveMode.STRAFE;
		this.robotWidth = robotWidth;
		this.robotHeight = robotHeight;
	}
	
	public void changeModeToRotate() {
		if (mode != DriveMode.ROTATE && !rotates.changingModes)
		{
			mode = DriveMode.ROTATE;
			rotates.setModeToRotate(robotWidth, robotHeight);
		}
	}
	
	public void changeModeToStrafe() {
		if (mode != DriveMode.STRAFE && !rotates.changingModes)
		{
			mode = DriveMode.STRAFE;
			rotates.setModeToStrafe(robotWidth, robotHeight);
		}
	}
	
	public void updateWithJoystickInput(JoystickInput input) {
		double inputDrive = input.getPolarRadius() * DRIVE_SCALE;
		double inputRotate = Math.toRadians(input.getPolarAngle());
		if (mode == DriveMode.STRAFE) {
			rotates.setTargetTheta((inputRotate + 2 * Math.PI) % (2 * Math.PI));
			for (int i = 0; i < drives.length; i++) {
				if (drives[i] != null) {
					drives[i].setSpeed(inputDrive);
				} else {
					System.out.println(i + " is null");
				}
			}
		} else if (mode == DriveMode.ROTATE) {
			for (int i = 0; i < drives.length; i++) {
				drives[i].setSpeed(Math.cos(inputRotate) * inputDrive);
			}
		} else {
			System.out.println("something has gone terribly wrong");
		}
	}
}




















