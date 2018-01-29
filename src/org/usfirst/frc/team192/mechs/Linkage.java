package org.usfirst.frc.team192.mechs;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

public class Linkage {

	private final double FLYWHEEL_SPEED_SCALE = 1.0;

	private TalonSRX linkage;
	private int ground_position = 0;
	private int switch_position = 0;
	private int scale_position = 0;
	public enum LinkagePosition {
		SWITCH,
		SCALE,
		GROUND
	}
	private LinkagePosition linkagePos;
	
	public Linkage(TalonSRX linkageMotor) {
		linkage = linkageMotor;
		linkagePos = LinkagePosition.GROUND;
	}

	public void moveToGroundPosition() {
		if (linkagePos != LinkagePosition.GROUND) {
		linkage.set(ControlMode.Position, ground_position);
		linkagePos = LinkagePosition.GROUND;
		System.out.println("linkage moved to ground position");
		}
	}
	
	public void moveToSwitchPosition() {
		if (linkagePos != LinkagePosition.SWITCH) {
		linkage.set(ControlMode.Position, switch_position);
		linkagePos = LinkagePosition.SWITCH;
		System.out.println("linkage moved to switch position");
		}
	}
	
	public void moveToScalePosition() {
		if (linkagePos != LinkagePosition.SCALE) {
		linkage.set(ControlMode.Position, scale_position);
		linkagePos = LinkagePosition.SCALE;
		System.out.println("linkage moved to scale position");
		}
	}
	
}
