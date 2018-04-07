package org.usfirst.frc.team192.robot;

import org.usfirst.frc.team192.mechs.Elevator;
import org.usfirst.frc.team192.mechs.Intake;
import org.usfirst.frc.team192.swerve.FullSwervePID;
import org.usfirst.frc.team192.swerve.SwerveBase;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.XboxController;

public class Teleop {
	private XboxController xboxMechs;
	private XboxController xboxSwerve;
	private Elevator elevator;
	private Intake intake;

	DigitalInput innerLimitSwitch;
	DigitalInput outerLimitSwitch;

	private FullSwervePID swerve;
	private VisionSwerve vision;
	private boolean useVision;

	public Teleop(FullSwervePID swerve, Intake intake, Elevator elevator, VisionSwerve vision) {
		xboxSwerve = new XboxController(0);
		xboxMechs = new XboxController(1);
		this.swerve = swerve;
		this.intake = intake;
		this.elevator = elevator;
		this.vision = vision;
	}

	public void init() {
	}

	public void periodic() {
		manualControls();
	}

	private void manualControls() {
		if (xboxMechs.getXButtonPressed()) {
			intake.moveCenterPickup();
		}
		if (xboxMechs.getBumperPressed(Hand.kRight)) {
			intake.movePickup();
		}
		if (xboxMechs.getBumperPressed(Hand.kLeft)) {
			elevator.breakElevator();
		}
		if (xboxMechs.getYButtonPressed()) {

		}

		intake.moveWheels(xboxMechs.getY(Hand.kLeft));
		double elevatorSpeed = SwerveBase.clip(-xboxMechs.getY(Hand.kRight));
		elevator.setSpeed(elevatorSpeed);
		if (elevatorSpeed != 0.0) {
			intake.movePickupOut();
		}
		swerve.updateWithJoystick(xboxSwerve);
	}
}
