package org.usfirst.frc.team192.fieldMapping;

import edu.wpi.first.wpilibj.interfaces.Gyro;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public abstract class FieldMapperAccel extends FieldMapperGyro {
	
	protected double vx;
	protected double vy;
	
	protected double lastAx;
	protected double lastAy;
	
	public FieldMapperAccel(Gyro gyro, double relX, double relY) {
		super(gyro, relX, relY);
		reset();
	}
	
	public void reset(double initX, double initY) {
		x = initX;
		y = initY;
		vx = 0;
		vy = 0;
		lastUpdated = System.currentTimeMillis();
	}
	
	public void reset() {
		reset(0, 0);
	}
	
	protected void updateAcceleration(double ax, double ay) {
		double lastVx = vx;
		double lastVy = vy;
		double dt = getDeltaTime();
		vx += (lastAx + ax) / 2 * dt;
		vy += (lastAy + ay) / 2 * dt;
		x += (lastVx + vx) / 2 * dt;
		y += (lastVy + vy) / 2 * dt;
		lastAx = ax;
		lastAy = ay; // lmao 
	}
	
}









