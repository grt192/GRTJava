package org.usfirst.frc.team192.mechs;

import com.ctre.CANTalon;
import com.ctre.CANTalon.TalonControlMode;

public class Collection {

	private final double DEFAULT_SPEED = 0.5;
	private CANTalon motor;
	private boolean on;

	public Collection(CANTalon motor1, CANTalon motor2) {
		motor = motor1;
		motor2.changeControlMode(TalonControlMode.Follower);
		motor2.set(motor1.getDeviceID());
		on = false;
	}

	public void set(double speed) {
		motor.set(speed);
		on = speed != 0.0;
	}

	public void set(boolean on) {
		motor.set(on ? DEFAULT_SPEED : 0.0);
		this.on = on;
	}

	public void toggle() {
		on = !on;
		set(on);
	}

}
