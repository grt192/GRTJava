package org.usfirst.frc.team192.fieldMapping;

import org.usfirst.frc.team192.swerve.NavXGyro;

public class FieldMapperNavXAccel extends FieldMapperAccel {
	
	private NavXGyro gyro;
	
	public FieldMapperNavXAccel(NavXGyro gyro) {
		super(gyro);
	}
	
	public void update() {
		updateAcceleration(gyro.getRawAccelX(), gyro.getRawAccelY());
	}
	
}
