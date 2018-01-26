package org.usfirst.frc.team192.mechs;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

public class Intake{

	private TalonSRX left, right, vertical;
	private int up_position = 0;
	private int down_position = 0;
	private int flywheel_speed = 0;
	
	public Intake(TalonSRX leftMotor, TalonSRX rightMotor, TalonSRX verticalMotor) {
		left = leftMotor;
		right = rightMotor;
		vertical = verticalMotor;
	}
	
	public void rollUp() {
		vertical.set(ControlMode.Position, up_position);
	}
	
	public void rollDown() {
		vertical.set(ControlMode.Position, down_position);
	}
	
	public void intake() {
		left.set(ControlMode.PercentOutput, flywheel_speed);
		right.set(ControlMode.PercentOutput, flywheel_speed);
	}
	
	public void reverseIntake() {
		left.set(ControlMode.PercentOutput, -flywheel_speed);
		left.set(ControlMode.PercentOutput, -flywheel_speed);
	}
}
