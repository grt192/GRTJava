package org.usfirst.frc.team192.fieldMapping;

import org.usfirst.frc.team192.swerve.NavXGyro;

public class FieldMapperNavXDisp implements FieldMapper {
	protected NavXGyro gyro;
	protected double dx;
	protected double dy;
	
	public FieldMapperNavXDisp(NavXGyro gyro) {
		this.gyro = gyro;
		reset();
	}
	
	public double getX() {
		return gyro.getDisplacementX() + dx;
	}
	
	public double getY() {
		return gyro.getDisplacementY() + dy;
	}
	
	public void reset(double x, double y) {
		gyro.resetDisplacement();
		dx = x;
		dy = y;
	}
	
	public void reset() {
		reset(0, 0);
	}
	
	public void update() {
		
	}
	
	public double getAngle() {
		return gyro.getAngle();
	}
}
