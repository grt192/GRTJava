package org.usfirst.frc.team192.robot;

import org.usfirst.frc.team192.robot.JoystickInput;
import org.usfirst.frc.team192.vision.VisionTracking;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import java.awt.Point;

import org.usfirst.frc.team192.mechs.*;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
public class Teleop {
	private XboxController xbox;
	private Elevator elevator;
	private Climber climber;
	private Intake intake;
	private VisionTracking vision;
	private Boolean is_vision_toggled;
	private Point centroid;
	private PIDController pid;
	
	DigitalInput innerLimitSwitch;
	DigitalInput outerLimitSwitch;
	Encoder eencoder;
	private int elevatorPos;
	private double k;
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
		elevator = new Elevator(new TalonSRX(1));
		climber = new Climber(new TalonSRX(8));
		intake = new Intake(new TalonSRX(0), new TalonSRX(2), new TalonSRX(3)); //add talon numbers for this
		is_vision_toggled = false;
		centroid = new Point();
		vision = new VisionTracking();
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
		elevatorPos = elevator.getElevatorPosition();
		eencoder = new Encoder(0, 1, false, Encoder.EncodingType.k4X); // 0 and 1 are digital input ports
																	  //false: don't invert counting direction
	}
	
	public void periodic() {
		if (xbox.getStartButton()) {
			visionToggleOn();
		}
		if (xbox.getBackButtonPressed()) {
			visionToggleOff();
		}
		if(xbox.getAButtonPressed()) {
			switchElevatorPlacement();
		}
		if(xbox.getBButtonPressed()) {
			scaleElevatorPlacement();
		}
		if(xbox.getYButtonPressed()) {
			groundElevatorPlacement();
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
				groundElevatorPlacement();
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
	
	public void switchElevatorPlacement() {
		elevator.moveToSwitchPosition();
		while(outerLimitSwitch.get()) {
			if(Math.abs(eencoder.get()) > k) /*speed check*/ {
				if (eencoder.get() < k) /*direction check*/{
					elevatorPos--;
				}else if (eencoder.get() > k) /*direction check*/ {
					elevatorPos++;
				}
			}
		}
	}
	
	public void scaleElevatorPlacement() {
		elevator.moveToScalePosition();
		while (outerLimitSwitch.get()) {
			if (Math.abs(elevatorPos - 2) > 1) /*distance (how many limit switches should be passed) check*/{
				elevatorPos++;
			} else if(eencoder.get() > k /*directional velocity check*/){
				elevatorPos++;
			}
		}
	}
	
	public void groundElevatorPlacement() {
		elevator.moveToGroundPosition();
		while(innerLimitSwitch.get()) {
			if(elevatorPos >= 1 && eencoder.get() < k) {
				elevatorPos--;
			}else if(eencoder.get() < k /*directional velocity check*/) {
				elevatorPos--;
			}
		}
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
