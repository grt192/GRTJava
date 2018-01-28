package org.usfirst.frc.team192.robot;

import org.usfirst.frc.team192.robot.JoystickInput;
import org.usfirst.frc.team192.vision.Vision;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import java.awt.Point;

import org.usfirst.frc.team192.mechs.*;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;

public class Teleop {
	private XboxController xbox;
	private Linkage linkage;
	private Climber climber;
	private Intake intake;
	private Vision vision;
	private Boolean is_vision_toggled;
	private Point centroid;
	private PIDController pid;
	
	public enum RobotState{
		NothingState,
		StartPickupState,
		MovingToBlock,
		PickingUpBlock,
		EndPickup
	}

	private RobotState pickupState = RobotState.NothingState;

	public Teleop(JoystickInput input) {
		xbox = input.getXboxController();
		linkage = new Linkage(new TalonSRX(1));
		climber = new Climber(new TalonSRX(8));
		intake = new Intake(new TalonSRX(0), new TalonSRX(2), new TalonSRX(3)); //add talon numbers for this
		is_vision_toggled = false;
		centroid = new Point();
		vision = new Vision();
		init();
		
		double P = 0.02;
		double I = 0.0001;
		double D = 0.5;
		double f = 0.0;
		
//		pid = new PIDController(P, I, D, f, gyro, this);
//		pid.setContinuous();
//		pid.setInputRange(0.0, 360.0);
//		pid.setAbsoluteTolerance(3.0);
//		pid.setOutputRange(-1.0, 1.0);
//		pid.reset();
//		pid.setSetpoint(0.0);

	}
	
	public void init() {
		//start swerve thread
		//start vision thread
		vision.cameraThread();
	}
	
	public void periodic() {
		
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
			scaleLinkagePlacement();
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
