package org.usfirst.frc.team192.fieldMapping;

import edu.wpi.first.wpilibj.interfaces.Gyro;

public abstract class FieldMapperAccel extends FieldMapperGyro {
	private double x;
	private double y;
	private Gyro gyro;
	private long lastUpdated;
	
	private double vx;
	private double vy;
	
	public FieldMapperAccel(Gyro gyro) {
		super(gyro);
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
		long dt = getDeltaTime();
		vx += ax * dt;
		vy += ay * dt;
		x += vx * dt;
		y += vy * dt;
	}
	
}









