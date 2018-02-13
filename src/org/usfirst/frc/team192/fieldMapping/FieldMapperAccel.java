package org.usfirst.frc.team192.fieldMapping;

import edu.wpi.first.wpilibj.interfaces.Gyro;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public abstract class FieldMapperAccel extends FieldMapperGyro {
	
	protected double vx;
	protected double vy;
	
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
		double dt = getDeltaTime();
		vx += ax * dt;
		SmartDashboard.putNumber("vel x", vx);
		vy += ay * dt;
		SmartDashboard.putNumber("vel y", vy);
		x += vx * dt;
		SmartDashboard.putNumber("x", x);
		y += vy * dt;
		SmartDashboard.putNumber("y", y);
	}
	
}









