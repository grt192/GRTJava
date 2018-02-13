package org.usfirst.frc.team192.fieldMapping;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public abstract class FieldMapperBase implements FieldMapper {
	protected long lastUpdated;
	protected double x;
	protected double y;
	
	protected double getDeltaTime() {
		long now = System.currentTimeMillis();
		long result = now - lastUpdated;
		lastUpdated = now;
		return result / 1000.0;
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
}
