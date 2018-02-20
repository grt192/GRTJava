package org.usfirst.frc.team192.mechs;

import org.usfirst.frc.team192.config.Config;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.XboxController;

public class Elevator {

	private final double FLYWHEEL_SPEED_SCALE = 1.0;

	private TalonSRX elevator;
	private TalonSRX follower;
	private Solenoid winchLock;
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
	
	public Elevator() {
		elevator = new TalonSRX(Config.getInt("winch_motor"));
		follower = new TalonSRX(Config.getInt("winch_motor_follower"));
		winchLock = new Solenoid(Config.getInt("winchsol"));
		elevatorPos = ElevatorPosition.GROUND;
		follower.set(ControlMode.Follower, elevator.getDeviceID());
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
			}else if(elevatorPos == ElevatorPosition.SCALE) {
				elevator.set(ControlMode.Velocity, 0 /*negative? parabolic velocity thing from scale to ground*/);
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
		
		setSpeed(speed);	
	}
	
	public void setSpeed(double speed) {
		if (Math.abs(speed) > 0.1) {
			winchLock.set(false);
			elevator.set(ControlMode.PercentOutput, speed);
		} else {
			elevator.set(ControlMode.PercentOutput, 0.0);
			winchLock.set(true);
		}
	}
	
}
