package org.usfirst.frc.team192.mechs;

import org.usfirst.frc.team192.config.Config;
import org.usfirst.frc.team192.robot.Robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.Solenoid;

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
		GROUND, ABOVEGROUND, SWITCH, SCALE
	}

	private ElevatorPosition elevatorPos;

	public Elevator() {
		elevator = new TalonSRX(Config.getInt("winch_motor"));
		elevator.setNeutralMode(NeutralMode.Brake);
		follower = new TalonSRX(Config.getInt("winch_motor_follower"));
		follower.setNeutralMode(NeutralMode.Coast);
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

	public void setPosition(double height) {
		elevator.set(ControlMode.Position, height);
	}

	public int getHeight() {
		return elevator.getSelectedSensorPosition(0);
	}

	public void moveToGroundPosition() {
		if (elevatorPos != ElevatorPosition.GROUND) {
			if (elevatorPos == ElevatorPosition.SWITCH) {
				elevator.set(ControlMode.Velocity, 0 /* negative? parabolic velocity thing from switch to ground */);
			} else if (elevatorPos == ElevatorPosition.SCALE) {
				elevator.set(ControlMode.Velocity, 0 /* negative? parabolic velocity thing from scale to ground */);
			}
		}
	}

	public void moveToSwitchPosition() {
		if (elevatorPos != ElevatorPosition.SWITCH) {
			if (elevatorPos == ElevatorPosition.GROUND) {
				elevator.set(ControlMode.Velocity, 0 /* positive? parabolic velocity thing from ground to switch */);
				follower.set(ControlMode.Follower, elevator.getDeviceID());
			} else if (elevatorPos == ElevatorPosition.SCALE) {
				elevator.set(ControlMode.Velocity, 0 /* negative? parabolic velocity thing from scale to switch */);
				follower.set(ControlMode.Follower, elevator.getDeviceID());
			}
		}
	}

	public void moveToScalePosition() {
		if (elevatorPos != ElevatorPosition.SCALE) {
			if (elevatorPos == ElevatorPosition.GROUND) {
				elevator.set(ControlMode.Velocity, 0/* positive? parabolic velocity thing from ground to scale */);
				follower.set(ControlMode.Follower, elevator.getDeviceID());
			} else if (elevatorPos == ElevatorPosition.SWITCH) {
				elevator.set(ControlMode.Velocity, 0/* positive? parabolic velocity thing from switch to scale */);
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

	public void setSpeed(double speed) {
		speed *= Math.max(1.0, Robot.timeSinceLastBrownout() / 2000.0) * 0.5;
		elevator.set(ControlMode.PercentOutput, speed);
	}

	public void breakElevator() {
		winchLock.set(!winchLock.get());
	}

	public void autonSetSpeed(double speed) {
		elevator.set(ControlMode.PercentOutput, speed);
	}

}
