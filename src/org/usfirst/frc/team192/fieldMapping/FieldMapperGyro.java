package org.usfirst.frc.team192.fieldMapping;

import edu.wpi.first.wpilibj.interfaces.Gyro;

public abstract class FieldMapperGyro extends FieldMapperBase {
	protected Gyro gyro;
	
	public FieldMapperGyro(Gyro gyro, double relX, double relY) {
		this.gyro = gyro;
		this.relX = relX;
		this.relY = relY;
	}
	
	public double getAngle() {
		if (gyro != null) {
			return ((gyro.getAngle() / 360 * 2 * Math.PI) % (2 * Math.PI) + 2 * Math.PI) % (2 * Math.PI);
		}
		return 0;
	}
	
	public double getX() {
		double angle = getAngle();
		double relAngle = Math.atan2(relY, relX);
		double r = Math.sqrt(relX * relX + relY * relY);
		double dx = r * Math.cos(angle + relAngle);
		return x - dx;
	}
	
	public double getY() {
		double angle = getAngle();
		double relAngle = Math.atan2(relY, relX);
		double r = Math.sqrt(relX * relX + relY * relY);
		double dy = r * Math.sin(angle + relAngle);
		return y - dy;
	}
}
