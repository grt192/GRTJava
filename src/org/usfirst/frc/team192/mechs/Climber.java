package org.usfirst.frc.team192.mechs;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.Solenoid;

public class Climber {
	private TalonSRX winch;
	public Climber(TalonSRX winchMotor, Solenoid winchSol) {
		winch = winchMotor;
	}
	
	public void climb() {
		winch.set(ControlMode.PercentOutput, 0.5);
		System.out.println("robot climbing");
	}
	
	public void stopClimb() {
		winch.set(ControlMode.PercentOutput, 0);
		System.out.println("robot stopped climbing");
	}
	
}
