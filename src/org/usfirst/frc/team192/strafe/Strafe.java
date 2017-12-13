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
	
	public Strafe(WheelDriveThread frontRightDrive, WheelRotateThread frontRightRotate, WheelDriveThread frontLeftDrive, WheelRotateThread frontLeftRotate, WheelDriveThread backRightDrive, WheelRotateThread backRightRotate, WheelDriveThread backLeftDrive, WheelRotateThread backLeftRotate) {
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
	}
	
	public void updateWithJoystickInput(JoystickInput input) {
		double inputDrive = input.getPolarRadius() * DRIVE_SCALE;
		double inputRotate = input.getPolarAngle();
		for (int i = 0; i < rotates.length; i++) {
			if (rotates[i] != null && inputDrive >= 0.2) {
				rotates[i].setTargetTheta((Math.toRadians(inputRotate) + 2 * Math.PI) % (2 * Math.PI));
			}
			if (drives[i] != null) {
				drives[i].setSpeed(inputDrive);
			}
		}
	}
}




















