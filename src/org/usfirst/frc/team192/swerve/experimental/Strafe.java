package org.usfirst.frc.team192.swerve.experimental;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;

public class Strafe {

	private Wheel[] wheels;

	public Strafe() {
		wheels = new Wheel[4];
		wheels[0] = new Wheel(new TalonSRX(1), new TalonSRX(2), null);// new DigitalInput(2));
		// wheels[1] = new Wheel(new TalonSRX(7), new TalonSRX(8), new DigitalInput(3));
		wheels[2] = new Wheel(new TalonSRX(9), new TalonSRX(10), null);// new DigitalInput(0));
		// wheels[3] = new Wheel(new TalonSRX(15), new TalonSRX(16), null);// new
		// DigitalInput(1));
		for (Wheel wheel : wheels)
			if (wheel != null)
				wheel.initialize();
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
		for (Wheel wheel : wheels) {
			if (wheel == null)
				continue;
			if (speed > 0.1)
				wheel.setTargetPosition(angle);
			if (speed < 0.1)
				wheel.setDriveSpeed(0.0);
			else
				wheel.setDriveSpeed(speed);
		}
	}

}
