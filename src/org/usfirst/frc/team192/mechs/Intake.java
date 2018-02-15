package org.usfirst.frc.team192.mechs;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

public class Intake{

	private TalonSRX left, right, vertical;
	private int up_position = 0;
	private int down_position = 0;
	private int flywheel_speed = 0;
	public enum IntakePosition{
		RAISED,
		LOWERED
	}
	public IntakePosition intakePos;
	public Intake(TalonSRX leftMotor, TalonSRX rightMotor, TalonSRX verticalMotor) {
		left = leftMotor;
		right = rightMotor;
		vertical = verticalMotor;
		intakePos = IntakePosition.LOWERED;
	}
	
	public void rollUp() {
		if (intakePos == IntakePosition.LOWERED) {
			vertical.set(ControlMode.Position, up_position);
			intakePos = IntakePosition.RAISED;
		}
	}
	
	public void rollDown() {
		if (intakePos == IntakePosition.RAISED) {
		vertical.set(ControlMode.Position, down_position);
		intakePos = IntakePosition.LOWERED;
		}
	}
	
	public void takeIn() {
		left.set(ControlMode.PercentOutput, flywheel_speed);
		right.set(ControlMode.PercentOutput, flywheel_speed);
	}
	
	public void reverse() {
		left.set(ControlMode.PercentOutput, -flywheel_speed);
		left.set(ControlMode.PercentOutput, -flywheel_speed);
	}
	
	public IntakePosition getIntakePosition() {
		return intakePos;
	}
}
