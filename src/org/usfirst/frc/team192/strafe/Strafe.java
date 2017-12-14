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
	private WheelRotateThread[] rotates;
	private double DRIVE_SCALE;
	private double robotWidth;
	private double robotHeight;
	
	private enum DriveMode {
		STRAFE, ROTATE
	}
	
	private DriveMode mode;
	
	public Strafe(WheelDriveThread frontRightDrive, WheelRotateThread frontRightRotate, WheelDriveThread frontLeftDrive, WheelRotateThread frontLeftRotate, WheelDriveThread backRightDrive, WheelRotateThread backRightRotate, WheelDriveThread backLeftDrive, WheelRotateThread backLeftRotate, double robotWidth, double robotHeight) {
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
		
		rotates = new WheelRotateThread[4];
		rotates[0] = frontRightRotate;
		rotates[1] = frontLeftRotate;
		rotates[2] = backRightRotate;
		rotates[3] = backLeftRotate;
		
		DRIVE_SCALE = 1 / Math.sqrt(2);
		mode = DriveMode.STRAFE;
		this.robotWidth = robotWidth;
		this.robotHeight = robotHeight;
	}
	
	public void changeModeToRotate() {
		mode = DriveMode.ROTATE;
		double initialTheta = Math.atan2(robotHeight, robotWidth);
		rotates[2].setTargetTheta(initialTheta);
		rotates[0].setTargetTheta(initialTheta + Math.PI / 2);
		rotates[1].setTargetTheta(initialTheta + Math.PI);
		rotates[3].setTargetTheta(initialTheta + 3 * Math.PI / 2);
	}
	
	public void changeModeToStrafe() {
		mode = DriveMode.STRAFE;
	}
	
	public void updateWithJoystickInput(JoystickInput input) {
		double inputDrive = input.getPolarRadius() * DRIVE_SCALE;
		double inputRotate = Math.toRadians(input.getPolarAngle());
		if (mode == DriveMode.STRAFE) {
			for (int i = 0; i < rotates.length; i++) {
				if (rotates[i] != null && inputDrive >= 0.1) {
					rotates[i].setTargetTheta((inputRotate + 2 * Math.PI) % (2 * Math.PI));
				}
				if (drives[i] != null) {
					drives[i].setSpeed(inputDrive);
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




















