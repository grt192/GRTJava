package org.usfirst.frc.team192.fieldMapping;

import org.usfirst.frc.team192.swerve.NavXGyro;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class FieldMapperNavXAccel extends FieldMapperAccel {
	
	protected NavXGyro gyro;
	
	public FieldMapperNavXAccel(NavXGyro gyro, double relX, double relY) {
		super(null, relX, relY);
		this.gyro = gyro;
	}
	
	public void update() {
		double accelX = gyro.getWorldLinearAccelX();
		double accelY = gyro.getWorldLinearAccelY();
		SmartDashboard.putNumber("accel x", accelX);
		SmartDashboard.putNumber("accel y", accelY);
		updateAcceleration(accelX, accelY);
	}
	
}
