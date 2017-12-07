package org.usfirst.frc.team192.mechs.gperiod;

import com.ctre.CANTalon;
import com.ctre.CANTalon.TalonControlMode;

public class Collection {

	private CANTalon motor;

	public Collection(CANTalon motor1, CANTalon motor2) {
		motor = motor1;
		motor2.changeControlMode(TalonControlMode.Follower);
		motor2.set(motor1.getDeviceID());
	}

	public void set(double speed) {
		motor.set(speed);
	}

}
