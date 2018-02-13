package org.usfirst.frc.team192.fieldMapping;

import org.usfirst.frc.team192.swerve.NavXGyro;

public class FieldMapperNavXVel extends FieldMapperVel {
	
	protected NavXGyro gyro;
	
	public FieldMapperNavXVel(NavXGyro gyro) {
		super(null);
		this.gyro = gyro;
	}
	
	public void update() {
		if (gyro != null) {
			updateVelocity(gyro.getVelocityX(), gyro.getVelocityY());
		}
	}
	
}
