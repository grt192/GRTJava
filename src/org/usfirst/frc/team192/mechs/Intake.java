package org.usfirst.frc.team192.mechs;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.Solenoid;

public class Intake{

	private TalonSRX innerLeft, innerRight, outerLeft, outerRight;
	private Solenoid leftSol, rightSol;
	private int inner_flywheel_speed = 50;
	private int outer_flywheel_speed = 100;
	
	public Intake(TalonSRX innerLeftMotor, TalonSRX innerRightMotor, 
			Solenoid rightpneumatic, Solenoid leftpneumatic, TalonSRX outerLeftMotor,
			TalonSRX outerRightMotor) {
		
		innerLeft = innerLeftMotor;
		outerLeft = outerLeftMotor;
		innerRight = innerRightMotor;
		outerRight = outerRightMotor;
		leftSol = leftpneumatic;
		rightSol = rightpneumatic;
	}
	
	
	public void rightTakeIn() {
		innerLeft.set(ControlMode.PercentOutput, inner_flywheel_speed);
		innerRight.set(ControlMode.PercentOutput, inner_flywheel_speed);
		outerLeft.set(ControlMode.PercentOutput, outer_flywheel_speed);
		outerRight.set(ControlMode.PercentOutput, outer_flywheel_speed);
		leftSol.set(true);
		rightSol.set(false);
	}

	public void leftTakeIn() {
		innerLeft.set(ControlMode.PercentOutput, inner_flywheel_speed);
		innerRight.set(ControlMode.PercentOutput, inner_flywheel_speed);
		outerLeft.set(ControlMode.PercentOutput, outer_flywheel_speed);
		outerRight.set(ControlMode.PercentOutput, outer_flywheel_speed);
		leftSol.set(false);
		rightSol.set(true);
	}
	
	public void centerTakeIn() {
		innerLeft.set(ControlMode.PercentOutput, inner_flywheel_speed);
		innerRight.set(ControlMode.PercentOutput, inner_flywheel_speed);
		outerLeft.set(ControlMode.PercentOutput, outer_flywheel_speed);
		outerRight.set(ControlMode.PercentOutput, outer_flywheel_speed);
		leftSol.set(false);
		rightSol.set(false);
	}
	
	public void clamp() {
		innerLeft.set(ControlMode.PercentOutput, inner_flywheel_speed);
		innerRight.set(ControlMode.PercentOutput, inner_flywheel_speed);
		outerLeft.set(ControlMode.PercentOutput, outer_flywheel_speed);
		outerRight.set(ControlMode.PercentOutput, outer_flywheel_speed);
		leftSol.set(true);
		rightSol.set(true);
	}
	
	public void reverse() {
		innerLeft.set(ControlMode.PercentOutput, -inner_flywheel_speed);
		innerRight.set(ControlMode.PercentOutput, -inner_flywheel_speed);
		outerLeft.set(ControlMode.PercentOutput, -outer_flywheel_speed);
		outerRight.set(ControlMode.PercentOutput, -outer_flywheel_speed);
		leftSol.set(true);
		rightSol.set(true);
	}
	
	public void nuetral() {
		innerLeft.set(ControlMode.PercentOutput, 0);
		innerRight.set(ControlMode.PercentOutput, 0);
		outerLeft.set(ControlMode.PercentOutput, 0);
		outerRight.set(ControlMode.PercentOutput, 0);
		leftSol.set(true);
		rightSol.set(true);
	}
}
