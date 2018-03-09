package org.usfirst.frc.team192.fieldMapping;

import edu.wpi.first.wpilibj.interfaces.Gyro;

public abstract class FieldMapperThreadVel extends FieldMapperGyro implements Runnable {
	
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
			try {
				Thread.sleep(15);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	protected abstract double getVx();
	protected abstract double getVy();
	
}

















