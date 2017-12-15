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

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;

public class JoystickInput {
	private Joystick turnStick;
	private XboxController xbox;

	public JoystickInput(int turnStickPort, int xboxPort) {
		turnStick = new Joystick(turnStickPort);
		xbox = new XboxController(xboxPort);
	}

	// get button pressing info
	public boolean getShooterButton() {
		return xbox.getAButton();
	}

	public boolean getLeverButton() {
		return xbox.getBButton();
	}

	public boolean getChalupaButton() {
		return xbox.getXButton();
	}

	public boolean getCollectionButton() {
		return xbox.getYButton();
	}

	public XboxController getXboxController() {
		return xbox;
	}

	public double getClippedX(Hand hand) {
		double val = xbox.getX(hand);
		return Math.abs(val) > 0.1 ? val : 0;
	}

	public double getClippedY(Hand hand) {
		double val = xbox.getY(hand);
		return Math.abs(val) > 0.1 ? val : 0;
	}

	// gets driver axis info
	public double getTurnStickX() {
		return turnStick.getX();
	}

	public double getTurnStickY() {
		return turnStick.getY();
	}

	// gets polar coordinates info
	public double getPolarRadius() {
		return Math.sqrt(Math.pow(getTurnStickX(), 2) + Math.pow(getTurnStickY(), 2));
	}

	public double getPolarAngle() {
		return Math.toDegrees(-1*Math.atan2(getTurnStickX(),getTurnStickY()));
	}

}
