package org.usfirst.frc.team192.fieldMapping;

import org.usfirst.frc.team192.swerve.FullSwerve;
import org.usfirst.frc.team192.swerve.SwerveData;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.interfaces.Gyro;

public class FieldMapperEncoder extends FieldMapperGyro {
	private FullSwerve swerve;
	private double lastVx;
	private double lastVy;
	private double dt;
	
	public FieldMapperEncoder(Gyro gyro, FullSwerve swerve) {
		super(gyro, 0, 0);
		this.swerve = swerve;
		dt = 0.05;
		reset();
	}
	
	public void run() {
		while (true) {
			SwerveData swerveData = swerve.getSwerveData();
			updateVelocity(swerveData.encoderVX, swerveData.encoderVY);
			Timer.delay(dt);
		}
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









