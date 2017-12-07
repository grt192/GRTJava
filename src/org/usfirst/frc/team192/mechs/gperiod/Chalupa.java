package org.usfirst.frc.team192.mechs.gperiod;

import com.ctre.CANTalon;

public class Chalupa {

	private final double DEFAULT_SPEED = 0.5;

	private CANTalon motor;
	private boolean on;

	public Chalupa(CANTalon motor) {
		this.motor = motor;
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
