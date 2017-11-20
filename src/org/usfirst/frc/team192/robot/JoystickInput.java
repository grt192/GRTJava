package org.usfirst.frc.team192.robot;
/* THIS COULD BE USEFUL
boolean exampleBool;
exampleBool = DriverStation.getInstance().isDSAttached();
exampleBool = DriverStation.getInstance().isFMSAttached();
exampleBool = DriverStation.getInstance().isSysActive();
exampleBool = DriverStation.getInstance().isSysBrownedOut();
<<<<<<< HEAD
=======

>>>>>>> chela
double time;
time = DriverStation.getInstance().getMatchTime();
*/

import edu.wpi.first.wpilibj.Joystick;

public class JoystickInput {
	public Joystick turnStick;
	public Joystick xbox;
	
	public JoystickInput() {
		turnStick = new Joystick(0);
		xbox = new Joystick(1);
	}
	
	//get button pressing info
	public boolean getShooterButton() {
		return xbox.getRawButton(1);
	}
	
	public boolean getLeverButton() {
		return xbox.getRawButton(2);
	}
	
	//gets driver axis info
	public double getTurnStickX() {
		return turnStick.getX();
	}
	
	public double getTurnStickY() {
		return turnStick.getY();
	}
	
	//gets polar coordinates info
	public double getPolarRadius() {
		return Math.sqrt(Math.pow(getTurnStickX(), 2) + Math.pow(getTurnStickY(), 2));
	}
	
	public double getPolarAngle() {
		return Math.toDegrees(Math.atan(getTurnStickY())/getTurnStickX());
	}

}
