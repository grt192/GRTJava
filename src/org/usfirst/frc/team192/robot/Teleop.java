package org.usfirst.frc.team192.robot;

import org.usfirst.frc.team192.mechs.Climber;
import org.usfirst.frc.team192.mechs.Elevator;
import org.usfirst.frc.team192.mechs.Intake;
import org.usfirst.frc.team192.swerve.FullSwervePID;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.XboxController;

public class Teleop {
	private XboxController xboxMechs;
	private XboxController xboxSwerve;
	private Elevator elevator;
	private Climber climber;
	private Intake intake;

	DigitalInput innerLimitSwitch;
	DigitalInput outerLimitSwitch;

	private FullSwervePID swerve;

	public Teleop(FullSwervePID swerve, Intake intake, Elevator elevator, VisionSwerve visionSwerve) {
		xboxSwerve = new XboxController(0);
		xboxMechs = new XboxController(1);
		this.swerve = swerve;
		this.intake = intake;
		this.elevator = elevator;
	}

	public void init() {
	}

	public void periodic() {
		if (xboxMechs.getXButtonPressed()) {
			intake.moveCenterPickup(xboxMechs);

		}
		if (xboxMechs.getBumperPressed(Hand.kRight)) {
			intake.movePickup();
		}
		if (xboxMechs.getBumperPressed(Hand.kLeft)) {
			elevator.breakElevator();
		}

		intake.moveWheels(xboxMechs);
		elevator.manualControl(xboxMechs);
		if (xboxMechs.getY() < -.1) {
			intake.movePickupOut();
		}
		swerve.updateWithJoystick(xboxSwerve);
	}
}
