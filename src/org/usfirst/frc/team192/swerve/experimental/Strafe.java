package org.usfirst.frc.team192.swerve.experimental;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.DigitalInput;

public class Strafe {

	private Wheel[] wheels;

	public Strafe() {
		wheels = new Wheel[4];
		wheels[0] = new Wheel(new CANTalon(1), new CANTalon(2), new DigitalInput(0));
		wheels[1] = new Wheel(new CANTalon(7), new CANTalon(8), new DigitalInput(1));
		wheels[2] = new Wheel(new CANTalon(9), new CANTalon(10), new DigitalInput(0));
		wheels[3] = new Wheel(new CANTalon(15), new CANTalon(16), new DigitalInput(2));
	}

	public void enable() {
		for (Wheel wheel : wheels)
			if (wheel != null)
				wheel.enable();
	}

	public void disable() {
		for (int i = 0; i < wheels.length; i++) {
			if (wheels[i] == null)
				continue;
			wheels[i].disable();
			wheels[i] = wheels[i].copy();
		}
	}

	public void update(double angle, double speed) {
		if (speed < 0.2)
			return;
		for (Wheel wheel : wheels) {
			if (wheel == null)
				continue;
			wheel.setTargetPosition(angle);
			wheel.setDriveSpeed(speed);
		}
	}

}
