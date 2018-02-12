package org.usfirst.frc.team192.fieldMapping;

public abstract class FieldMapperBase implements FieldMapper {
	private long lastUpdated;
	private double x;
	private double y;
	
	protected long getDeltaTime() {
		long now = System.currentTimeMillis();
		long result = now - lastUpdated;
		lastUpdated = now;
		return result;
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
}
