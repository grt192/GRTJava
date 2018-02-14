package org.usfirst.frc.team192.fieldMapping;

import org.usfirst.frc.team192.swerve.FullSwerve;
import org.usfirst.frc.team192.swerve.SwerveData;

import edu.wpi.first.wpilibj.interfaces.Gyro;

public class FieldMapperEncoder extends FieldMapperVel {
	private FullSwerve swerve;
	
	public FieldMapperEncoder(Gyro gyro, FullSwerve swerve) {
		super(gyro, 0, 0);
		this.swerve = swerve;
	}
	
	public void update() {
		SwerveData swerveData = swerve.getSwerveData();
		updateVelocity(swerveData.encoderVX, swerveData.encoderVY);
	}
}









