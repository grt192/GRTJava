package org.usfirst.frc.team192.fieldMapping;

import edu.wpi.first.wpilibj.interfaces.Gyro;

public abstract class FieldMapperThreadVel extends FieldMapperGyro implements Runnable {
	
	private double x;
	private double y;
	
	public FieldMapperThreadVel(Gyro gyro, double relX, double relY) {
		super(gyro, relX, relY);
		reset();
	}
	
	public void reset(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public void reset() {
		reset(0, 0);
	}
	
	public void run() {
		while (true) {
			double dt = getDeltaTime();
			x += getVx() * dt;
			y += getVy() * dt;
		}
	}
	
	protected abstract double getVx();
	protected abstract double getVy();
	
}

















