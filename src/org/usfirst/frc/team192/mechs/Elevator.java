package org.usfirst.frc.team192.mechs;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

public class Elevator {

	private final double FLYWHEEL_SPEED_SCALE = 1.0;

	private TalonSRX Elevator;
	private int ground_position = 0;
	private int switch_position = 0;
	private int scale_position = 0;
	public enum ElevatorPosition {
		SWITCH,
		SCALE,
		GROUND
	}
	private ElevatorPosition ElevatorPos;
	
	public Elevator(TalonSRX ElevatorMotor) {
		Elevator = ElevatorMotor;
		ElevatorPos = ElevatorPosition.GROUND;
	}

	public void moveToGroundPosition() {
		if (ElevatorPos != ElevatorPosition.GROUND) {
		Elevator.set(ControlMode.Position, ground_position);
		ElevatorPos = ElevatorPosition.GROUND;
		System.out.println("Elevator moved to ground position");
		}
	}
	
	public void moveToSwitchPosition() {
		if (ElevatorPos != ElevatorPosition.SWITCH) {
		Elevator.set(ControlMode.Position, switch_position);
		ElevatorPos = ElevatorPosition.SWITCH;
		System.out.println("Elevator moved to switch position");
		}
	}
	
	public void moveToScalePosition() {
		if (ElevatorPos != ElevatorPosition.SCALE) {
		Elevator.set(ControlMode.Position, scale_position);
		ElevatorPos = ElevatorPosition.SCALE;
		System.out.println("Elevator moved to scale position");
		}
	}
	
}
