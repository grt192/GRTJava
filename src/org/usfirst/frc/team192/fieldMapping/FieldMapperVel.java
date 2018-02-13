package org.usfirst.frc.team192.fieldMapping;

import edu.wpi.first.wpilibj.interfaces.Gyro;

public abstract class FieldMapperVel extends FieldMapperGyro {
	
	public FieldMapperVel(Gyro gyro) {
		super(gyro);
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
		x += vx * dt;
		y += vy * dt;
	}
}










