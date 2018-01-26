package org.usfirst.frc.team192.mechs;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

public class Linkage {

	private final double FLYWHEEL_SPEED_SCALE = 1.0;

	private TalonSRX linkage;
	private int ground_position = 0;
	private int switch_position = 0;
	private int scale_position = 0;

	public Linkage(TalonSRX linkageMotor) {
		linkage = linkageMotor;
	}

	public void moveToGroundPosition() {
		linkage.set(ControlMode.Position, ground_position);
		System.out.println("linkage moved to ground position");
	}
	
	public void moveToSwitchPosition() {
		linkage.set(ControlMode.Position, switch_position);
		System.out.println("linkage moved to switch position");
	}
	
	public void moveToScalePosition() {
		linkage.set(ControlMode.Position, scale_position);
		System.out.println("linkage moved to scale position");
	}
	
}
