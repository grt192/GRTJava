package org.usfirst.frc.team192.fieldMapping;

import org.usfirst.frc.team192.swerve.NavXGyro;

public class FieldMapperNavXDisp extends FieldMapperGyro {
	protected NavXGyro gyro;
	protected double dx;
	protected double dy;
	
	public FieldMapperNavXDisp(NavXGyro gyro, double relX, double relY) {
		super(gyro, relX, relY);
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
		x = gyro.getDisplacementX() + dx;
		y = gyro.getDisplacementY() + dy;
	}
	
	public double getAngle() {
		return gyro.getAngle();
	}
}
