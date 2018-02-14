package org.usfirst.frc.team192.fieldMapping;

import edu.wpi.first.wpilibj.interfaces.Accelerometer;
import edu.wpi.first.wpilibj.interfaces.Gyro;

public class FieldMapperAccelerometer extends FieldMapperAccel {
	protected Accelerometer accel;
	
	public FieldMapperAccelerometer(Gyro gyro, Accelerometer accelerometer, double relX, double relY) {
		super(gyro, relX, relY);
		accel = accelerometer;
	}
	
	public void update() {
		updateAcceleration(accel.getX(), accel.getY());
	}
	
}









