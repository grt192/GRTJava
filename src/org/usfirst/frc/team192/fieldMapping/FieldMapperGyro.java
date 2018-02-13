package org.usfirst.frc.team192.fieldMapping;

import edu.wpi.first.wpilibj.interfaces.Gyro;

public abstract class FieldMapperGyro extends FieldMapperBase {
	protected Gyro gyro;
	
	public FieldMapperGyro(Gyro gyro) {
		this.gyro = gyro;
	}
	
	public double getAngle() {
		if (gyro != null) {
			return ((gyro.getAngle() / 360 * 2 * Math.PI) % (2 * Math.PI) + 2 * Math.PI) % (2 * Math.PI);
		}
		return 0;
	}
}
