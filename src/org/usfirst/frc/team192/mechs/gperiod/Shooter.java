package org.usfirst.frc.team192.mechs.gperiod;

import com.ctre.CANTalon;

public class Shooter {

	private final double FLYWHEEL_SPEED_SCALE = 1.0;

	private CANTalon flywheel;
	private CANTalon turntable;

	public Shooter(CANTalon flywheelMotor, CANTalon turntableMotor) {
		// Will also need something else to push the balls into the flywheel

		flywheel = flywheelMotor;
		turntable = turntableMotor;
	}

	public void shoot() {

	}

	public void setFlywheelSpeed(double speed) {
		flywheel.set(speed);
	}

	public double getFlywheelSpeed() {
		return flywheel.getEncVelocity() * FLYWHEEL_SPEED_SCALE;
	}

	public void rotateByAngle(double theta) {
		// won't implement unless necessary for vision
	}

	public void setTurnTableSpeed(double speed) {
		turntable.set(speed);
	}

}
