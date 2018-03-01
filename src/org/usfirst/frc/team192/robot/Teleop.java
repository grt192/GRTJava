package org.usfirst.frc.team192.robot;

import org.usfirst.frc.team192.mechs.Climber;
import org.usfirst.frc.team192.mechs.Elevator;
import org.usfirst.frc.team192.mechs.Intake;
import org.usfirst.frc.team192.swerve.FullSwervePID;
import org.usfirst.frc.team192.vision.nn.RemoteVisionThread;

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
	private RemoteVisionThread vision;
	private VisionSwerve visionSwerve;
	private boolean usingController;

	DigitalInput innerLimitSwitch;
	DigitalInput outerLimitSwitch;
	//Encoder eencoder;

	private FullSwervePID swerve;
//  private boolean zeroing;
//	private int index;
	
	//private Point CAMCENTER;
  
	public enum RobotState{
		NothingState,
		StartPickupState,
		MovingToBlock,
		PickingUpBlock,
		ClampBlock,
		EndPickup
	}
	
	private RobotState destination;
	private RobotState pickupState = RobotState.NothingState;

	public Teleop(FullSwervePID swerve, Intake intake, Elevator elevator, VisionSwerve visionSwerve) {
		xboxSwerve = new XboxController(0);
		xboxMechs = new XboxController(1);
		//climber = new Climber());
		// 1 is the gear, 0 is main, 2 is the right
		this.swerve = swerve;
		this.vision = vision;
		//CAMCENTER.x = 320;
		//CAMCENTER.y = 240;
		this.intake = intake;
		this.elevator = elevator;
		this.visionSwerve = visionSwerve;
		usingController = true;
		init();

//		this.zeroing = false;
	}

	public void init() {
		// 0 and 1 are digital input ports
		//eencoder = new Encoder(0, 1, false, Encoder.EncodingType.k4X); 
	}

	public void periodic() {
		//climb code
//		if (xbox.getXButtonPressed()) {
//			climber.climb();
//		}
//		if (xbox.getXButtonReleased()) {
//			climber.stopClimb();
//		}
		//pickup code
		if (xboxMechs.getXButtonPressed()) {
			intake.moveCenterPickup(xboxMechs);
		}
		if (xboxMechs.getBumperPressed(Hand.kRight)) {
			intake.movePickup();
		}
		if (xboxMechs.getBumperPressed(Hand.kLeft)) {
			elevator.breakElevator();
		}
		
		//right trigger
		intake.moveWheels(xboxMechs);
		
		//elevator code
		//right xbox axis
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
