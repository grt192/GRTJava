package org.usfirst.frc.team192.robot;

import org.usfirst.frc.team192.mechs.Climber;
import org.usfirst.frc.team192.mechs.Elevator;
import org.usfirst.frc.team192.mechs.Intake;
import org.usfirst.frc.team192.swerve.FullSwervePID;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj.XboxController;

public class Teleop {
	private XboxController xboxMechs;
	private XboxController xboxSwerve;
	private Elevator elevator;
	private Climber climber;
	private Intake intake;
	private VisionSwerve visionSwerve;
	private boolean usingController;

	DigitalInput innerLimitSwitch;
	DigitalInput outerLimitSwitch;
	// Encoder eencoder;

	private FullSwervePID swerve;
	// private boolean zeroing;
	// private int index;

	// private Point CAMCENTER;

	public enum RobotState {
		NothingState, StartPickupState, MovingToBlock, PickingUpBlock, ClampBlock, EndPickup
	}

	private RobotState destination;
	private RobotState pickupState = RobotState.NothingState;

	public Teleop(FullSwervePID swerve, Intake intake, Elevator elevator, VisionSwerve visionSwerve) {
		xboxSwerve = new XboxController(0);
		xboxMechs = new XboxController(1);
		this.swerve = swerve;
		this.intake = intake;
		this.elevator = elevator;
		this.visionSwerve = visionSwerve;
		usingController = true;
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

		if (xboxSwerve.getBButtonPressed()) {
			usingController = !usingController;
			if (usingController) {
				visionSwerve.kill();
			}
		}

		if (usingController) {
			swerve.updateWithJoystick(xboxSwerve);
		} else {
			usingController = !visionSwerve.update();
			if (usingController) {
				visionSwerve.kill();
				xboxMechs.setRumble(RumbleType.kLeftRumble, 0.5);
				xboxSwerve.setRumble(RumbleType.kLeftRumble, 0.5);
			}
		}
	}
}
