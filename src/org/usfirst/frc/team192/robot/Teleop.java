package org.usfirst.frc.team192.robot;

import java.awt.Point;

import org.usfirst.frc.team192.config.Config;
import org.usfirst.frc.team192.mechs.Climber;
import org.usfirst.frc.team192.mechs.Intake;
import org.usfirst.frc.team192.mechs.Elevator;
import org.usfirst.frc.team192.swerve.FullSwervePID;
import org.usfirst.frc.team192.vision.ImageThread;
import org.usfirst.frc.team192.vision.nn.RemoteVisionThread;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.XboxController;

public class Teleop {
	private XboxController xbox;
	private Elevator elevator;
	private Climber climber;
	private Intake intake;
	private RemoteVisionThread vision;

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

	public Teleop(FullSwervePID swerve, Intake intake, Elevator elevator) {
		xbox = new XboxController(1);
		//climber = new Climber());
		// 1 is the gear, 0 is main, 2 is the right
		this.swerve = swerve;
		this.vision = vision;
		//CAMCENTER.x = 320;
		//CAMCENTER.y = 240;
		this.intake = intake;
		this.elevator = elevator;
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
		if (xbox.getXButtonPressed()) {
			intake.moveCenterPickup(xbox);
		}
		if (xbox.getBumperPressed(Hand.kRight)) {
			intake.movePickup();
		}
		if (xbox.getBumperPressed(Hand.kLeft)) {
			elevator.breakElevator();
		}
		
		//right trigger
		intake.moveWheels(xbox);
		
		//elevator code
		//right xbox axis
		elevator.manualControl(xbox);
		if (xbox.getY() < -.1) {
			intake.movePickupOut();
		}
		
		//vision code
	/*	if (xbox.getAButtonPressed()) {
			pickupState = RobotState.StartPickupState;

			switch (pickupState) {
				case NothingState:
					break;
				case StartPickupState:
					elevator.moveToGroundPosition();
					if (vision.hasTarget()) {
						pickupState = RobotState.MovingToBlock;
					}
				case MovingToBlock:
					double errorx = vision.getCenter().x - CAMCENTER.x;
					swerve.setWithAngularVelocity(0, 0, errorx);
					swerve.updateAutonomous();
					if (Math.abs(errorx) < 10) {
						pickupState = RobotState.PickingUpBlock;
					}
				case PickingUpBlock:
					double errory = vision.getCenter().y - CAMCENTER.y;
					intake.autonPickup();
					swerve.setWithAngularVelocity(0, errory, 0);
					if (Math.abs(errory) < 10) {
						pickupState = RobotState.ClampBlock;
					}
				case ClampBlock:
					double errory2 = vision.getCenter().y - CAMCENTER.y;
					intake.autonClamp();
					if (errory2 < -10) {
						pickupState = RobotState.EndPickup;
					}
				case EndPickup:
					System.out.println("block picked up");
				}
		}
*/	
//	if (!zeroing && xbox.getAButton() && xbox.getXButton()) {
//	zeroing = true;
//	index = 0;
//	System.out.println("zeroing");
//	return;
//}
//
//if (zeroing && xbox.getBButtonPressed()) {
//	index++;
//	System.out.println("changing wheels");
//	if (index == 4) {
//		zeroing = false;
//		swerve.zero();
//		return;
//	}
//}
//
//if (zeroing) {
//	swerve.zeroWithInputs(index, xbox);
//	return;
//}
	}
}
