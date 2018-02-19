package org.usfirst.frc.team192.robot;

import org.usfirst.frc.team192.mechs.Climber;
import org.usfirst.frc.team192.mechs.Intake;
import org.usfirst.frc.team192.mechs.Elevator;
import org.usfirst.frc.team192.swerve.FullSwervePID;
import org.usfirst.frc.team192.vision.ImageThread;
import org.usfirst.frc.team192.vision.VisionThread;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.GyroBase;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.XboxController;
public class Teleop {
	private XboxController xbox;
	private Elevator elevator;
	private Climber climber;
	private Intake intake;
//	private ImageThread img;
//	private Boolean is_vision_toggled;

	DigitalInput innerLimitSwitch;
	DigitalInput outerLimitSwitch;
	Encoder eencoder;
  
//  private Point centroid;
	private int elevatorPos;
	private double k;
//  private VisionPID pid;
//	private FullSwervePID swerve;
//  private boolean zeroing;
//	private int index;
  
	public enum RobotState{
		NothingState,
		StartPickupState,
		MovingToBlock,
		PickingUpBlock,
		EndPickup
	}
	
	private RobotState destination;

	private RobotState pickupState = RobotState.NothingState;

//	public Teleop(VisionThread vision, FullSwervePID swerve, JoystickInput input, GyroBase gyro) {
	public Teleop(JoystickInput input) {
		xbox = input.getXboxController();
		elevator = new Elevator(new TalonSRX(12), new TalonSRX(13));
		climber = new Climber(new TalonSRX(8));
		intake = new Intake(new TalonSRX(5), new TalonSRX(14), new TalonSRX(3), new Solenoid(0), new Solenoid(3), new Solenoid(2)); // 1 is the gear, 0 is main, 2 is the right
//		is_vision_toggled = false;
//		pid = new VisionPID(vision, swerve, gyro);
//		this.swerve = swerve;
		init();

//		this.zeroing = false;

	}

	public void init() {
		elevatorPos = elevator.getElevatorPosition();
		//eencoder = new Encoder(0, 1, false, Encoder.EncodingType.k4X); // 0 and 1 are digital input ports
//		innerLimitSwitch = new DigitalInput(0);
//		outerLimitSwitch = new DigitalInput(1);	
		//false: don't invert counting direction
//		pid.PIDEnable();
	}

	public void periodic() {
//		if (!zeroing && xbox.getAButton() && xbox.getXButton()) {
//			zeroing = true;
//			index = 0;
//			System.out.println("zeroing");
//			return;
//		}
//
//		if (zeroing && xbox.getBButtonPressed()) {
//			index++;
//			System.out.println("changing wheels");
//			if (index == 4) {
//				zeroing = false;
//				swerve.zero();
//				return;
//			}
//		}
//
//		if (zeroing) {
//			swerve.zeroWithInputs(index, xbox);
//			return;
//		}
//
//		if (xbox.getYButtonPressed()) {
//			pid.PIDEnable();
//		}
//
//		double rotateRight = Math.pow(xbox.getTriggerAxis(Hand.kRight), 2);
//		double rotateLeft = Math.pow(xbox.getTriggerAxis(Hand.kLeft), 2);
//		double howMuchToRotate = rotateRight - rotateLeft;
//		if (xbox.getYButton()) {
//			// System.out.println("b button");
//			pid.update();
//			pid.updatePID();
//
//		} else if (Math.abs(howMuchToRotate) > 0.05) {
//			// System.out.println("trigger: " + howMuchToRotate / 2);
//			swerve.setWithAngularVelocity(0, 0, howMuchToRotate / 2);
//		} else {
//			swerve.setWithAngularVelocity(0, 0, 0);
//		}
//		swerve.updateAutonomous();
//
//		if (xbox.getStartButton()) {
//			visionToggleOn();
//		}
//		if (xbox.getBackButtonPressed()) {
//			visionToggleOff();
//		}
//		if(xbox.getAButtonPressed()) {
//			switchElevatorPlacement();
//		}
//		if(xbox.getBButtonPressed()) {
//			scaleElevatorPlacement();
//		}
//		if(xbox.getYButtonPressed()) {
//			groundElevatorPlacement();
//		}
//		if (xbox.getXButtonPressed()) {
//			// climb();
//		}
//		if (xbox.getXButtonReleased()) {
//			// stopClimb();
//		}
//		if (xbox.getTriggerAxis(Hand.kRight) > 0) {
//			// System.out.println(Double.toString(xbox.getTriggerAxis(Hand.kRight)));
//		}
//		if (xbox.getY(Hand.kRight))
//		if (xbox.getAButtonPressed()) {
//			this.pickupState = RobotState.StartPickupState;
//
//			switch (this.pickupState) {
//			case NothingState:
//				break;
//			case StartPickupState:
//				groundElevatorPlacement();
//				intakeDown();
//				intake();
//				// visionon
//				// getvisiondata
//
//				// if(getvisiondata) {
//				this.pickupState = RobotState.MovingToBlock;
//				// }
//			case MovingToBlock:
//				// getvisiondata
//				// if (visiondatareturn == movingorsomething) { PID?
//				// x = new pseudoJoystick
//				// getPseudoJoystick(visioninfo, x)
//				// sendtoswerve(joystick)
//				// }else if (visiondatareturn = reachedDestination){
//				// this.pickupState = RobotState.PickingUpBlock;
//				// }
//			case PickingUpBlock:
//				if (intakeVision()) {
//					this.pickupState = RobotState.EndPickup;
//				}
//			case EndPickup:
//				// visionoff;
//			}
//
//		}

		/*
		 * if (xbox.getAButton()) { SmartDashboard.putNumber("blockLocation",
		 * SmartDashboard.getNumber("blockLocation", 320) + 1); } if (xbox.getBButton())
		 * { SmartDashboard.putNumber("blockLocation",
		 * SmartDashboard.getNumber("blockLocation", 320) - 1); }
		 */

//		if(outerLimitSwitch.get()) {
//			if(Math.abs(eencoder.get()) > k) /*speed check*/ {
//				if (eencoder.get() < k) /*direction check*/{
//					elevatorPos--;
//				}else if (eencoder.get() > k) /*direction check*/ {
//					elevatorPos++;
//				}
//			}
//		}
//		
//		if(outerLimitSwitch.get()) {
//			if (Math.abs(elevatorPos - 2) > 1) /*distance (how many limit switches should be passed) check*/{
//				elevatorPos++;
//			} else if(eencoder.get() > k /*directional velocity check*/){
//				elevatorPos++;
//			}
//		}
//		
//		if(innerLimitSwitch.get()) {
//			if(elevatorPos >= 1 && eencoder.get() < k) {
//				elevatorPos--;
//			}else if(eencoder.get() < k /*directional velocity check*/) {
//				elevatorPos--;
//			}
//		}
//		elevator.setElevatorPosition(elevatorPos);
		if (xbox.getStickButtonPressed(Hand.kRight)) {
			stopWheels();
		}
		if (xbox.getBackButtonPressed()) {
			leftArmContract();
		}
		if (xbox.getStartButtonPressed()) {
			rightArmContract();
		}
		if (xbox.getYButtonPressed()) {
			intake();
			System.out.println("Y pressed");
		}

		if (xbox.getAButtonPressed()) {
			reverseIntake();
			System.out.println("A pressed");
		}
		
		if (xbox.getXButtonPressed()) {
			leftArmActuate();
			System.out.println("X pressed");
		}
		
		if (xbox.getBButtonPressed()) {
			rightArmActuate();
			System.out.println("B pressed");
		}
		
		if (xbox.getBumperPressed(Hand.kLeft)) {
			intakeExtend();
			System.out.println("LB pressed");
		}
		
		if (xbox.getBumperPressed(Hand.kRight)) {
			intakeContract();
			System.out.println("RB pressed");
		}
	}


	
	
	
	
	
	
	
	
//	public void visionToggleOn() {
//		is_vision_toggled = true;
//		System.out.println("vision toggle on");
//	}
//
//	public void visionToggleOff() {
//		is_vision_toggled = true;
//		System.out.println("vision toggle off");
//	}

	public void switchElevatorPlacement() {
		elevator.moveToSwitchPosition();
	}
	
	public void scaleElevatorPlacement() {
		elevator.moveToScalePosition();
	}
	
	public void groundElevatorPlacement() {
		elevator.moveToGroundPosition();
	}

	public void intakeExtend() {
		intake.rollOut();
	}

	public void intakeContract() {
		intake.rollIn();
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

	public void stopWheels() {
		intake.stopWheels();
	}
	public void leftArmActuate() {
		intake.leftActuate();
	}
	public void leftArmContract() {
		intake.leftContract();
	}
	public void rightArmContract() {
		intake.rightContract();
	}
	public void rightArmActuate() {
		intake.rightActuate();
	}
	public boolean exchangeVision() { // true if succeed, false if fails
		return false;
	}

	public boolean intakeVision() { // true if succeed, false if fails
		return false;
	}
}
