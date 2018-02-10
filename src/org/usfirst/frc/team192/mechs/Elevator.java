package org.usfirst.frc.team192.mechs;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

public class Elevator {

	private final double FLYWHEEL_SPEED_SCALE = 1.0;

	private TalonSRX elevator;
	private int ground_position = 0;
	private int switch_position = 0;
	private int scale_position = 0;
	private int above_ground_position = 0;
	public enum ElevatorPosition {
		SWITCH,
		SCALE,
		GROUND,
		ABOVEGROUND
	}
	private ElevatorPosition elevatorPos;
	
	public Elevator(TalonSRX elevatorMotor) {
		elevator = elevatorMotor;
		elevatorPos = ElevatorPosition.GROUND;
	}

	public void moveToGroundPosition() {
		if (elevatorPos != ElevatorPosition.GROUND) {
		elevator.set(ControlMode.Position, ground_position);
		elevatorPos = ElevatorPosition.GROUND;
		System.out.println("elevator moved to ground position");
		}
	}
	
	public void moveToSwitchPosition() {
		if (elevatorPos != ElevatorPosition.SWITCH) {
		elevator.set(ControlMode.Position, switch_position);
		elevatorPos = ElevatorPosition.SWITCH;
		System.out.println("elevator moved to switch position");
		}
	}
	
	public void moveToScalePosition() {
		if (elevatorPos != ElevatorPosition.SCALE) {
		elevator.set(ControlMode.Position, scale_position);
		elevatorPos = ElevatorPosition.SCALE;
		System.out.println("elevator moved to scale position");
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
