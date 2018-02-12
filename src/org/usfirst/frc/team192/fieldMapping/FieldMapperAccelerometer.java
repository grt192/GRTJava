package org.usfirst.frc.team192.fieldMapping;

import edu.wpi.first.wpilibj.interfaces.Accelerometer;
import edu.wpi.first.wpilibj.interfaces.Gyro;

public class FieldMapperAccelerometer extends FieldMapperAccel {
	
	private double x;
	private double y;
	private long lastUpdated;
	private Accelerometer accel;
	
	public FieldMapperAccelerometer(Gyro gyro, Accelerometer accelerometer) {
		super(gyro);
		accel = accelerometer;
	}
	
	public void update() {
		updateAcceleration(accel.getX(), accel.getY());
	}
	
}









