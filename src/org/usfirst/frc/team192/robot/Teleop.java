package org.usfirst.frc.team192.robot;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.GenericHID.Hand;

public class Teleop {

	private JoystickInput joysticks;
	private CANTalon motor;
	
	public Teleop() {
		joysticks = new JoystickInput(0, 1);
		motor = new CANTalon(12); //it might be 11
	}

	public void init() {

	}

	public void periodic() {
		doSwerve();
		motor.set(joysicks.getClippedY(Hand.kleft));
	}

	private void doSwerve() {
		// TODO: Do whatever swerve stuff here when Jonny pushes his code
	}
}
