package org.usfirst.frc.team192.fieldMapping;

import org.usfirst.frc.team192.swerve.FullSwerve;
import org.usfirst.frc.team192.swerve.SwerveData;

import edu.wpi.first.wpilibj.interfaces.Gyro;

public class FieldMapperThreadEncoder extends FieldMapperThreadVel {
	
	private FullSwerve swerve;
	
	public FieldMapperThreadEncoder(Gyro gyro, FullSwerve swerve) {
		super(gyro, 0, 0);
		this.swerve = swerve;
	}
	
	protected double getVx() {
		SwerveData data = swerve.getSwerveData();
		return data.encoderVX;
	}
	
	protected double getVy() {
		SwerveData data = swerve.getSwerveData();
		return data.encoderVY;
	}
}
