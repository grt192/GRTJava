package org.usfirst.frc.team192.mechs;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.XboxController;

public class Intake{

	private TalonSRX left, right, upper;
	private Solenoid mainSol, leftSol, rightSol;
	public boolean leftExtended;
	public boolean rightExtended;
	public boolean centerExtended;
	public Intake(TalonSRX lowerLeftMotor, TalonSRX lowerRightMotor, TalonSRX upperMotors, Solenoid mainSolenoid, Solenoid leftSolenoid, Solenoid rightSolenoid) {
		left = lowerLeftMotor;
		right = lowerRightMotor;
		upper = upperMotors;
		mainSol = mainSolenoid;
		leftSol = leftSolenoid;
		rightSol = rightSolenoid;
		
		rightExtended = false;
		leftExtended = false;
		centerExtended = false;
	}
	
	public void spitOut(XboxController xbox) {
		double speed = xbox.getY(Hand.kLeft);
		if(speed > 0) {
			upper.set(ControlMode.PercentOutput, speed);
			left.set(ControlMode.PercentOutput, -speed);
			right.set(ControlMode.PercentOutput, speed);
		}
	}
	
	public void moveInnerWheels(XboxController xbox) {
		double speed = xbox.getTriggerAxis(Hand.kLeft);
		upper.set(ControlMode.PercentOutput, speed);
	}
	
	public void moveOuterWheels(XboxController xbox) {
		double speed = xbox.getTriggerAxis(Hand.kRight);
		right.set(ControlMode.PercentOutput, -speed);
		left.set(ControlMode.PercentOutput, speed);
	}
	
	public void moveLeftPickup(XboxController xbox) {
		if (!leftExtended) {
			leftSol.set(true);
			leftExtended = true;
		}else {
			leftSol.set(false);
			leftExtended = false;
		}
	}
	
	public void moveRightPickup(XboxController xbox) {
		if (!rightExtended) {
			rightSol.set(true);
			rightExtended = true;
		}else {
			rightSol.set(false);
			rightExtended = false;
		}
	}
	
	public void moveBothPickupsOut() {
		if (!rightExtended) {
			rightSol.set(true);
			rightExtended = true;
		}
		if (!leftExtended) {
			leftSol.set(true);
			leftExtended = true;
		}
	}
	
	public void moveBothPickupsIn() {
		if (rightExtended) {
			rightSol.set(false);
			rightExtended = false;
		}
		if (leftExtended) {
			leftSol.set(false);
			leftExtended = false;
		}
	}
	
	public void moveCenterPickup(XboxController xbox) {
		if (!centerExtended) {
			mainSol.set(true);
			centerExtended = true;
		}else {
			leftSol.set(false);
			rightSol.set(false);
			mainSol.set(false);
			centerExtended = false;
			rightExtended = false;
			leftExtended = false;
		}
	}
	
	public void autonPickup() {
		if (!centerExtended) {
			mainSol.set(true);
			centerExtended = true;
		}
		if (!rightExtended) {
			rightSol.set(true);
			rightExtended = true;
		}
		if (!leftExtended) {
			leftSol.set(true);
			leftExtended = true;
		}
		upper.set(ControlMode.PercentOutput, 1);
		left.set(ControlMode.PercentOutput, -1);
		right.set(ControlMode.PercentOutput, 1);
		
	}
	
	public void autonClamp() {
		if (rightExtended) {
			rightSol.set(false);
			rightExtended = false;
		}
		if (leftExtended) {
			leftSol.set(false);
			leftExtended = false;
		}
	}
	
	public void stopAutonIntake() {
		upper.set(ControlMode.PercentOutput, 0);
		left.set(ControlMode.PercentOutput, 0);
		right.set(ControlMode.PercentOutput, 0);
	}
	
}
