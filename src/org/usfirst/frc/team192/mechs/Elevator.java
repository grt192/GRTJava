package org.usfirst.frc.team192.mechs;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.XboxController;

public class Elevator {

	private final double FLYWHEEL_SPEED_SCALE = 1.0;

	private TalonSRX elevator;
	private TalonSRX follower;
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
	
	public Elevator(TalonSRX elevatorMotor, TalonSRX elevatorFollow) {
		elevator = elevatorMotor;
		follower = elevatorFollow;
		elevatorPos = ElevatorPosition.GROUND;
	}
	public int getElevatorPosition() {
		return ElevatorPosition.valueOf(elevatorPos.toString()).ordinal();
	}
	
	public void setElevatorPosition(int i) {
		elevatorPos = ElevatorPosition.values()[i];
	}

	public void moveToGroundPosition() {
		if (elevatorPos != ElevatorPosition.GROUND) {
			if(elevatorPos == ElevatorPosition.SWITCH) {
				elevator.set(ControlMode.Velocity, 0  /*negative? parabolic velocity thing from switch to ground*/);
				follower.set(ControlMode.Follower, elevator.getDeviceID());
			}else if(elevatorPos == ElevatorPosition.SCALE) {
				elevator.set(ControlMode.Velocity, 0 /*negative? parabolic velocity thing from scale to ground*/);
				follower.set(ControlMode.Follower, elevator.getDeviceID());
			}
		}
	}
	
	public void moveToSwitchPosition() {
		if (elevatorPos != ElevatorPosition.SWITCH) {
			if(elevatorPos == ElevatorPosition.GROUND) {
				elevator.set(ControlMode.Velocity, 0 /*positive? parabolic velocity thing from ground to switch*/);
				follower.set(ControlMode.Follower, elevator.getDeviceID());
			}else if(elevatorPos == ElevatorPosition.SCALE) {
				elevator.set(ControlMode.Velocity, 0 /*negative? parabolic velocity thing from scale to switch*/);
				follower.set(ControlMode.Follower, elevator.getDeviceID());
			}
		}
	}
	
	public void moveToScalePosition() {
		if (elevatorPos != ElevatorPosition.SCALE) {
			if(elevatorPos == ElevatorPosition.GROUND) {
				elevator.set(ControlMode.Velocity, 0/*positive? parabolic velocity thing from ground to scale*/);
				follower.set(ControlMode.Follower, elevator.getDeviceID());
			}else if(elevatorPos == ElevatorPosition.SWITCH) {
				elevator.set(ControlMode.Velocity, 0/*positive? parabolic velocity thing from switch to scale */);
				follower.set(ControlMode.Follower, elevator.getDeviceID());
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
	
	public void manualControl(XboxController xbox) {
		double speed = xbox.getY(Hand.kRight);
		elevator.set(ControlMode.PercentOutput, speed);
		follower.set(ControlMode.Follower, elevator.getDeviceID());
	}
	
}
