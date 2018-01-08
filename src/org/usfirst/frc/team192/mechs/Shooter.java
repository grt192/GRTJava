package org.usfirst.frc.team192.mechs;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

public class Shooter {

	private final double FLYWHEEL_SPEED_SCALE = 1.0;

	private TalonSRX flywheel;
	private TalonSRX turntable;

	public Shooter(TalonSRX flywheelMotor, TalonSRX turntableMotor) {
		// Will also need something else to push the balls into the flywheel

		flywheel = flywheelMotor;
		turntable = turntableMotor;
	}

	public void shoot() {

	}

	public void setFlywheelSpeed(double speed) {
		flywheel.set(ControlMode.PercentOutput, speed);
	}

	public double getFlywheelSpeed() {
		return flywheel.getSensorCollection().getQuadraturePosition() * FLYWHEEL_SPEED_SCALE;
	}

	public void rotateByAngle(double theta) {
		// won't implement unless necessary for vision
	}

	public void setTurnTableSpeed(double speed) {
		turntable.set(ControlMode.PercentOutput, speed);
	}

}
