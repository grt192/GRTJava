package org.usfirst.frc.team192.robot;

import org.usfirst.frc.team192.vision.ImageThread;
import org.usfirst.frc.team192.vision.VisionTracking;
import org.usfirst.frc.team192.robot.VisionPID;
import org.usfirst.frc.team192.swerve.FullSwervePID;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import java.awt.Point;

import org.usfirst.frc.team192.mechs.*;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.GyroBase;

public class Teleop {
	private XboxController xbox;
	private Linkage linkage;
	private Climber climber;
	private Intake intake;
	private ImageThread img;
	private Boolean is_vision_toggled;
	private Point centroid;
	private VisionPID pid;
	private FullSwervePID swerve;
	
	private boolean zeroing;
	private int index;
	
	public enum RobotState{
		NothingState,
		StartPickupState,
		MovingToBlock,
		PickingUpBlock,
		EndPickup
	}

	private RobotState pickupState = RobotState.NothingState;

	public Teleop(VisionTracking vision, FullSwervePID swerve, JoystickInput input, GyroBase gyro) {
		xbox = input.getXboxController();
		linkage = new Linkage(new TalonSRX(1));
		climber = new Climber(new TalonSRX(8));
		intake = new Intake(new TalonSRX(0), new TalonSRX(2), new TalonSRX(3)); //add talon numbers for this
		is_vision_toggled = false;
		centroid = new Point();
		pid = new VisionPID(vision, swerve, gyro);
		this.swerve = swerve;
		init();
		
		this.zeroing = false;

	}
	
	public void init() {
		//start swerve thread
		pid.PIDEnable();
	}
	
	public void periodic() {
		
		if (!zeroing && xbox.getAButton() && xbox.getXButton()) {
			zeroing = true;
			index = 0;
			System.out.println("zeroing");
			return;
		}
		
		if (zeroing && xbox.getBButtonPressed()) {
			index++;
			System.out.println("changing wheels");
			if (index == 4) {
				zeroing = false;
				swerve.zero();
				return;
			}
		}
		
		if (zeroing) {
			swerve.zeroWithInputs(index, xbox);
			return;
		}
		
		if (xbox.getYButtonPressed()) {
			pid.PIDEnable();
		}
		
		double rotateRight = Math.pow(xbox.getTriggerAxis(Hand.kRight), 2);
		double rotateLeft = Math.pow(xbox.getTriggerAxis(Hand.kLeft), 2);
		double howMuchToRotate = rotateRight - rotateLeft;
		if (xbox.getYButton()) {
			//System.out.println("b button");
			pid.update();
			pid.updatePID();
			
		} else if (Math.abs(howMuchToRotate) > 0.05) {
			// System.out.println("trigger: " + howMuchToRotate / 2);
			swerve.setWithAngularVelocity(0, 0, howMuchToRotate / 2);
		} else {
			swerve.setWithAngularVelocity(0, 0, 0);
		}
		swerve.updateAutonomous();
		
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
			//climb();
		}
		if(xbox.getXButtonReleased()) {
			//stopClimb();
		}if(xbox.getTriggerAxis(Hand.kRight) > 0) {
			// System.out.println(Double.toString(xbox.getTriggerAxis(Hand.kRight)));						
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
		
		/*
		if (xbox.getAButton()) {
			SmartDashboard.putNumber("blockLocation", SmartDashboard.getNumber("blockLocation", 320) + 1);
		}
		if (xbox.getBButton()) {
			SmartDashboard.putNumber("blockLocation", SmartDashboard.getNumber("blockLocation", 320) - 1);
		}
		*/

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
