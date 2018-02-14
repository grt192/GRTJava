package org.usfirst.frc.team192.fieldMapping;

import edu.wpi.first.wpilibj.interfaces.Gyro;

public abstract class FieldMapperVel extends FieldMapperGyro {
	
	protected double lastVx;
	protected double lastVy;
	
	public FieldMapperVel(Gyro gyro, double relX, double relY) {
		super(gyro, relX, relY);
		reset();
	}
	
	public void reset(double initX, double initY) {
		x = initX;
		y = initY;
		lastUpdated = System.currentTimeMillis();
	}
	
	public void reset() {
		reset(0, 0);
	}
	
	protected void updateVelocity(double vx, double vy) {
		double dt = getDeltaTime();
		x += (vx + lastVx) / 2 * dt;
		y += (vy + lastVy) / 2 * dt;
		lastVx = vx;
		lastVy = vy;
	}
}










