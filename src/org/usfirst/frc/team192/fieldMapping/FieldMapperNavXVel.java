package org.usfirst.frc.team192.fieldMapping;

import org.usfirst.frc.team192.swerve.NavXGyro;

public class FieldMapperNavXVel extends FieldMapperVel {
	
	private NavXGyro gyro;
	
	public FieldMapperNavXVel(NavXGyro gyro) {
		super(gyro);
	}
	
	public void update() {
		updateVelocity(gyro.getVelocityX(), gyro.getVelocityY());
	}
	
}
