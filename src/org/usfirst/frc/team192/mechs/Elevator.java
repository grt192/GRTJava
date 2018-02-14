package org.usfirst.frc.team192.mechs;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.DigitalInput;

public class Elevator {

	private final double FLYWHEEL_SPEED_SCALE = 1.0;

	private TalonSRX elevator;
	private int ground_position = 0;
	private int switch_position = 0;
	private int scale_position = 0;
	private int above_ground_position = 0;
	public enum ElevatorPosition {
		GROUND,
		ABOVEGROUND,
		SWITCH,
		SCALE
	}
	private ElevatorPosition elevatorPos;
	
	public Elevator(TalonSRX elevatorMotor) {
		elevator = elevatorMotor;
		elevatorPos = ElevatorPosition.GROUND;
	}
	public int getElevatorPosition() {
		return ElevatorPosition.valueOf(elevatorPos.toString()).ordinal();
	}

	public void moveToGroundPosition() {
		if (elevatorPos != ElevatorPosition.GROUND) {
//		elevator.set(ControlMode.Position, ground_position);
//		elevatorPos = ElevatorPosition.GROUND;
//		System.out.println("elevator moved to ground position");
			if(elevatorPos == ElevatorPosition.SWITCH) {
				elevator.set(ControlMode.Velocity, 0  /*negative? parabolic velocity thing from switch to ground*/);
			}else if(elevatorPos == ElevatorPosition.SCALE) {
				elevator.set(ControlMode.Velocity, 0 /*negative? parabolic velocity thing from scale to ground*/);
			}
		}
	}
	
	public void moveToSwitchPosition() {
		if (elevatorPos != ElevatorPosition.SWITCH) {
//		elevator.set(ControlMode.Position, switch_position);
//		elevatorPos = ElevatorPosition.SWITCH;
//		System.out.println("elevator moved to switch position");
			if(elevatorPos == ElevatorPosition.GROUND) {
				elevator.set(ControlMode.Velocity, 0 /*positive? parabolic velocity thing from ground to switch*/);
			}else if(elevatorPos == ElevatorPosition.SCALE) {
				elevator.set(ControlMode.Velocity, 0 /*negative? parabolic velocity thing from scale to switch*/);
			}
		}
	}
	
	public void moveToScalePosition() {
		if (elevatorPos != ElevatorPosition.SCALE) {
//		elevator.set(ControlMode.Position, scale_position);
//		elevatorPos = ElevatorPosition.SCALE;
//		System.out.println("elevator moved to scale position");
			if(elevatorPos == ElevatorPosition.GROUND) {
				elevator.set(ControlMode.Velocity, 0/*positive? parabolic velocity thing from ground to scale*/);
			}else if(elevatorPos == ElevatorPosition.SWITCH) {
				elevator.set(ControlMode.Velocity, 0/*positive? parabolic velocity thing from switch to scale */);
			}
		}
	}
	
	public void moveToAboveGroundhPosition() {
		if (elevatorPos != ElevatorPosition.ABOVEGROUND) {
			elevator.set(ControlMode.Position, above_ground_position);
			elevatorPos = ElevatorPosition.ABOVEGROUND;
			System.out.println("elevator moved to above ground position");
		}
	
	}
	
}
