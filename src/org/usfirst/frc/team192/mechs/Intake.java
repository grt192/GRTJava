package org.usfirst.frc.team192.mechs;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.Solenoid;

public class Intake{

	private TalonSRX left, right, upper;
	private Solenoid mainSol, leftSol, rightSol;
	private int up_position = 0;
	private int down_position = 0;
	private double flywheel_speed = .5;
	private boolean wheelsMoving = false;
	public enum IntakePosition{
		EXTENDED,
		CONTRACTED
	}
	public IntakePosition intakePos;
	public Intake(TalonSRX lowerLeftMotor, TalonSRX lowerRightMotor, TalonSRX upperMotors, Solenoid mainSolenoid, Solenoid leftSolenoid, Solenoid rightSolenoid) {
		left = lowerLeftMotor;
		right = lowerRightMotor;
		upper = upperMotors;
		mainSol = mainSolenoid;
		leftSol = leftSolenoid;
		rightSol = rightSolenoid;
		
		intakePos = IntakePosition.CONTRACTED;
	}
	
	public void rollOut() {
		if (intakePos == IntakePosition.CONTRACTED) {
			mainSol.set(true);
			intakePos = IntakePosition.EXTENDED;
		}
	}
	
	public void rollIn() {
		if (intakePos == IntakePosition.EXTENDED) {
		left.set(ControlMode.Velocity, 0);
		right.set(ControlMode.Velocity, 0);
		upper.set(ControlMode.Velocity, 0);
		leftSol.set(false);
		rightSol.set(false);
		mainSol.set(false);
		intakePos = IntakePosition.CONTRACTED;
		}
	}
	
	public void takeIn() {
		if (intakePos == IntakePosition.EXTENDED) {
				left.set(ControlMode.PercentOutput, flywheel_speed);
				right.set(ControlMode.PercentOutput, -flywheel_speed);
				upper.set(ControlMode.PercentOutput, 1);
				wheelsMoving = true;
		}
	}
	public void stopWheels() {
		left.set(ControlMode.PercentOutput, 0);
		right.set(ControlMode.Follower, left.getDeviceID());
		upper.set(ControlMode.PercentOutput, 0);	
	}
	public void reverse() {
		if (intakePos == IntakePosition.EXTENDED) {
				left.set(ControlMode.PercentOutput, -flywheel_speed);
				right.set(ControlMode.PercentOutput, flywheel_speed);
				upper.set(ControlMode.PercentOutput, -1);
		}
	}
	
	public void leftActuate() {
		if (intakePos == IntakePosition.EXTENDED) {
			leftSol.set(true);
		}
	}
	
	public void leftContract() {
		if (intakePos == IntakePosition.EXTENDED) {
			leftSol.set(false);
		}
	}
	
	
	public void rightActuate() {
		if (intakePos == IntakePosition.EXTENDED) {
			rightSol.set(true);
		}
	}
	public void rightContract() {
		if (intakePos == IntakePosition.EXTENDED) {
			rightSol.set(false);
		}
	}
	public IntakePosition getIntakePosition() {
		return intakePos;
	}
}
