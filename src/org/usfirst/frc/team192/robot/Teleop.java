package org.usfirst.frc.team192.robot;

import java.awt.Point;

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
import edu.wpi.first.wpilibj.interfaces.Gyro;

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
	
	private Point CAMCENTER;
  
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

	public Teleop(RemoteVisionThread vision, FullSwervePID swerve, Gyro gyro) {
		xbox = new XboxController(1);
		elevator = new Elevator(new TalonSRX(12), new TalonSRX(13));
		climber = new Climber(new TalonSRX(8));
		// 1 is the gear, 0 is main, 2 is the right
		intake = new Intake(new TalonSRX(5), new TalonSRX(14), 
				new TalonSRX(3), new Solenoid(0), new Solenoid(3), 
				new Solenoid(2)); 
		this.swerve = swerve;
		this.vision = vision;
		CAMCENTER.x = 320;
		CAMCENTER.y = 240;
		init();

//		this.zeroing = false;
	}

	public void init() {
		// 0 and 1 are digital input ports
		//eencoder = new Encoder(0, 1, false, Encoder.EncodingType.k4X); 
	}

	public void periodic() {
		//climb code
		if (xbox.getXButtonPressed()) {
			climber.climb();
		}
		if (xbox.getXButtonReleased()) {
			climber.stopClimb();
		}
		//pickup code
		if (xbox.getBumperPressed(Hand.kRight)) {
			intake.moveRightPickup(xbox);
		}
		if (xbox.getBumperPressed(Hand.kLeft)) {
			intake.moveLeftPickup(xbox);
		}
		if (xbox.getBumperPressed(Hand.kRight) && 
				xbox.getBumperPressed(Hand.kLeft)) {
			intake.moveCenterPickup(xbox);
		}
		//left xbox axis
		intake.spitOut(xbox);
		//left trigger
		intake.moveInnerWheels(xbox);
		//right trigger
		intake.moveOuterWheels(xbox);
		
		//elevator code
		//right xbox axis
		elevator.manualControl(xbox);
		
		//vision code
		if (xbox.getAButtonPressed()) {
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
