package org.usfirst.frc.team192.robot;

import java.awt.Point;

import org.usfirst.frc.team192.mechs.Climber;
import org.usfirst.frc.team192.mechs.Elevator;
import org.usfirst.frc.team192.mechs.Intake;
import org.usfirst.frc.team192.swerve.FullSwervePID;
import org.usfirst.frc.team192.vision.ImageThread;
import org.usfirst.frc.team192.vision.VisionThread;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.GyroBase;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.XboxController;
public class Teleop {
	private XboxController xbox;
	private Elevator elevator;
	private Climber climber;
	private Intake intake;
	private ImageThread img;
	private Boolean is_vision_toggled;

	DigitalInput innerLimitSwitch;
	DigitalInput outerLimitSwitch;
	Encoder eencoder;
  
	private Point centroid;
	private int elevatorPos;
	private double k;
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

	public Teleop(VisionThread vision, FullSwervePID swerve, JoystickInput input, GyroBase gyro) {
		xbox = input.getXboxController();
		pid = new VisionPID(vision, swerve, gyro);
		pid.PIDEnable();
		
		is_vision_toggled = false;
		this.swerve = swerve;
		this.zeroing = false;
		
		init();
	}

	public void init() {
		elevator = new Elevator(new TalonSRX(1));
		climber = new Climber(new TalonSRX(1), new Solenoid(0));
		intake = new Intake(new TalonSRX(3), new TalonSRX(4), new Solenoid(1), 
				new Solenoid(2), new TalonSRX(5), new TalonSRX(6));
		
		elevatorPos = elevator.getElevatorPosition();
		 // 0 and 1 are digital input ports, false: don't invert counting direction
		
		
		eencoder = new Encoder(0, 1, false, Encoder.EncodingType.k4X);
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
			// System.out.println("b button");
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
			switchElevatorPlacement();
		}
		if(xbox.getBButtonPressed()) {
			scaleElevatorPlacement();
		}
		if(xbox.getYButtonPressed()) {
			groundElevatorPlacement();
		}
		if (xbox.getXButtonPressed()) {
			// climb();
		}
		if (xbox.getXButtonReleased()) {
			// stopClimb();
		}
		if (xbox.getTriggerAxis(Hand.kRight) > 0) {
			// System.out.println(Double.toString(xbox.getTriggerAxis(Hand.kRight)));
		}
		if (xbox.getAButtonPressed()) {
			this.pickupState = RobotState.StartPickupState;

			switch (this.pickupState) {
			case NothingState:
				break;
			case StartPickupState:
				groundElevatorPlacement();
				intake();
				// visionon
				// getvisiondata

				// if(getvisiondata) {
				this.pickupState = RobotState.MovingToBlock;
				// }
			case MovingToBlock:
				// getvisiondata
				// if (visiondatareturn == movingorsomething) { PID?
				// x = new pseudoJoystick
				// getPseudoJoystick(visioninfo, x)
				// sendtoswerve(joystick)
				// }else if (visiondatareturn = reachedDestination){
				// this.pickupState = RobotState.PickingUpBlock;
				// }
			case PickingUpBlock:
				if (intakeVision()) {
					this.pickupState = RobotState.EndPickup;
				}
			case EndPickup:
				// visionoff;
			}

		}

		/*
		 * if (xbox.getAButton()) { SmartDashboard.putNumber("blockLocation",
		 * SmartDashboard.getNumber("blockLocation", 320) + 1); } if (xbox.getBButton())
		 * { SmartDashboard.putNumber("blockLocation",
		 * SmartDashboard.getNumber("blockLocation", 320) - 1); }
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

	public boolean exchangeVision() { // true if succeed, false if fails
		return false;
	}

	public boolean intakeVision() { // true if succeed, false if fails
		return false;
	}
}
