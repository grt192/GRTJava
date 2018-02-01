package org.usfirst.frc.team192.robot;

import java.awt.Point;

import org.usfirst.frc.team192.mechs.Climber;
import org.usfirst.frc.team192.mechs.Intake;
import org.usfirst.frc.team192.mechs.Linkage;
import org.usfirst.frc.team192.swerve.FullSwervePID;
import org.usfirst.frc.team192.vision.ImageThread;
import org.usfirst.frc.team192.vision.VisionTracking;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.GyroBase;
import edu.wpi.first.wpilibj.XboxController;

public class Teleop {
	private XboxController xbox;
	private Linkage linkage;
	private Climber climber;
	private Intake intake;
	private VisionTracking vision;
	private Boolean is_vision_toggled;
	private Point centroid;
	private VisionPID pid;
	private FullSwervePID swerve;
	
	public enum RobotState{
		NothingState,
		StartPickupState,
		MovingToBlock,
		PickingUpBlock,
		EndPickup
	}

	private RobotState pickupState = RobotState.NothingState;

	public Teleop(JoystickInput input, GyroBase gyro) {
		xbox = input.getXboxController();
		linkage = new Linkage(new TalonSRX(1));
		climber = new Climber(new TalonSRX(8));
		intake = new Intake(new TalonSRX(0), new TalonSRX(2), new TalonSRX(3)); //add talon numbers for this
		is_vision_toggled = false;
		centroid = new Point();
		vision = new VisionTracking();
		swerve = new FullSwervePID(gyro);
		pid = new VisionPID(vision, swerve, new ImageThread());
		init();

	}
	
	public void init() {
		//start swerve thread
	}
	
	public void periodic() {
		
		if (xbox.getBButtonPressed()) {
			System.out.println("b button");
			pid.PIDEnable();
		}
		
		if (xbox.getBButton()) {
			double range = 10;
			if (pid.pidGet() > (Math.abs(Math.abs(pid.getCamCenter()) - 10))) {
				pid.updateWithVision();
				swerve.updateAutonomous();
			}
		}
		
		if (xbox.getBButtonReleased()) {
			swerve.setWithAngularVelocity(0, 0, 0);
		}
		
		if (xbox.getStartButton()) {
			visionToggleOn();
		}
		if (xbox.getBackButtonPressed()) {
			visionToggleOff();
		}
		if(xbox.getAButtonPressed()) {
			switchLinkagePlacement();
		}
		if(xbox.getBButtonPressed()) {
			//scaleLinkagePlacement();
		}
		if(xbox.getYButtonPressed()) {
			groundLinkagePlacement();
		}
		if(xbox.getXButtonPressed()) {
			climb();
		}
		if(xbox.getXButtonReleased()) {
			stopClimb();
		}if(xbox.getTriggerAxis(Hand.kRight) > 0) {
			System.out.println(Double.toString(xbox.getTriggerAxis(Hand.kRight)));						
		}
		if(xbox.getAButtonPressed()) {
			this.pickupState = RobotState.StartPickupState;
			
			switch(this.pickupState) {
			case NothingState:
				break;
			case StartPickupState:
				groundLinkagePlacement();
				intakeDown();
				intake();
				//visionon
				//getvisiondata
				
//				if(getvisiondata) {
					this.pickupState = RobotState.MovingToBlock;
//				}
			case MovingToBlock:
//				getvisiondata
//				if (visiondatareturn == movingorsomething) { PID?
//					x = new pseudoJoystick
//					getPseudoJoystick(visioninfo, x)
//					sendtoswerve(joystick)
//				}else if (visiondatareturn = reachedDestination){
//				this.pickupState = RobotState.PickingUpBlock;
//				}
			case PickingUpBlock:
				if (intakeVision()) {
					this.pickupState = RobotState.EndPickup;
				}
			case EndPickup:
//				visionoff;
			}
			
		}
	}
	
	public void visionToggleOn() {
		is_vision_toggled = true;
		System.out.println("vision toggle on");
	}
	
	public void visionToggleOff() {
		is_vision_toggled = true;
		System.out.println("vision toggle off");
	}
	
	public void switchLinkagePlacement() {
		linkage.moveToSwitchPosition();
	}
	
	public void scaleLinkagePlacement() {
		linkage.moveToScalePosition();
	}
	
	public void groundLinkagePlacement() {
		linkage.moveToGroundPosition();
	}
	
	public void intakeDown() {
		intake.rollDown();
	}
	
	public void intakeUp() {
		intake.rollUp();
	}
	
	public void intake() {
		intake.takeIn();
	}
	
	public void reverseIntake() {
		intake.reverse();
	}
	
	public void climb() {
		climber.climb();
	}
	
	public void stopClimb() {
		climber.stopClimb();
	}
	
	public boolean exchangeVision() { //true if succeed, false if fails
		return false;
	}

	public boolean intakeVision() { //true if succeed, false if fails
		return false;
	}
}
