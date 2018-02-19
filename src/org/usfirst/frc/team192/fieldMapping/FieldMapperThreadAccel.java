package org.usfirst.frc.team192.fieldMapping;

import edu.wpi.first.wpilibj.interfaces.Gyro;

public abstract class FieldMapperThreadAccel extends FieldMapperGyro implements Runnable {
	private double x;
	private double y;
	private double vx;
	private double vy;
	
	public FieldMapperThreadAccel(Gyro gyro, double relX, double relY) {
		super(gyro, relX, relY);
		reset();
	}
	
	public void reset(double x, double y) {
		this.x = x;
		this.y = y;
		vx = 0;
		vy = 0;
	}
	
	public void reset() {
		reset(0, 0);
	}
	
	public void run() {
		while (true) {
			double dt = getDeltaTime();
			vx += getAx() * dt;
			vy += getAy() * dt;
			x += vx * dt;
			y += vy * dt;
		}
	}
	
	protected abstract double getAx();
	protected abstract double getAy();
}


















