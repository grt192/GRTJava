package org.usfirst.frc.team192.fieldMapping;

public interface FieldMapper {
	
	public double getX();
	public double getY();
	public double getAngle();
	public void update();
	public void reset();
	public void reset(double initX, double initY);
}
