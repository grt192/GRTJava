package org.usfirst.frc.team192.fieldMapping;

import edu.wpi.first.wpilibj.interfaces.Gyro;

public abstract class FieldMapperGyro extends FieldMapperBase {
	private Gyro gyro;
	
	public FieldMapperGyro(Gyro gyro) {
		this.gyro = gyro;
	}
	
	public double getAngle() {
		return ((gyro.getAngle() / 360 * 2 * Math.PI) % (2 * Math.PI) + 2 * Math.PI) % (2 * Math.PI);
	}
}
