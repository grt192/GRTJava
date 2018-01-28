package org.usfirst.frc.team192.swerve;

public class SwerveData {

	public final double gyroAngle, gyroRate, encoderRate, encoderVX, encoderVY;

	public SwerveData(double gyroAngle, double gyroRate, double encAngVel, double encVX, double encVY) {
		this.gyroAngle = gyroAngle;
		this.gyroRate = gyroRate;
		encoderRate = encAngVel;
		encoderVX = encVX;
		encoderVY = encVY;
	}

}
