package org.usfirst.frc.team192.fieldMapping;

public interface FieldMapper extends Runnable {
	
	public double getX();
	public double getY();
	public double getAngle();
	public void reset();
	public void reset(double initX, double initY);
}
