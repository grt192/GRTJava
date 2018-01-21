package org.usfirst.frc.team192.robot;

import org.usfirst.frc.team192.robot.JoystickInput;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import org.usfirst.frc.team192.mechs.*;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.XboxController;

public class Teleop {
	private XboxController xbox;
	private Linkage linkage;
	private Boolean is_vision_toggled;
	private int button_toggle_counter;

	public Teleop(JoystickInput input) {
		xbox = input.getXboxController();
		linkage = new Linkage(new TalonSRX(3));
		is_vision_toggled = false;
		button_toggle_counter = 0;
	}

	public void init() {
		
	}
	
	public void periodic() {
		if (xbox.getStartButton()) {
			visionToggleOn();
		}
		else if (xbox.getBackButton()) {
			visionToggleOff();
		}
		else if(xbox.getAButton()) {
			switchLinkagePlacement();
		}
		else if(xbox.getBButton()) {
			scaleLinkagePlacement();
		}
		else if(xbox.getYButton()) {
			groundLinkagePlacement();
		}
	}
	
	public void visionToggleOn() {
		is_vision_toggled = true;
		System.out.println("vision toggle on");
	}
	
	public void visionToggleOff() {
		is_vision_toggled = true;
		System.out.println("vision toggle off");
	}
	
	public void switchLinkagePlacement() {
		linkage.moveToSwitchPosition();
	}
	
	public void scaleLinkagePlacement() {
		linkage.moveToScalePosition();
	}
	
	public void groundLinkagePlacement() {
		linkage.moveToGroundPosition();
	}
	
	
	public void blockPickup() {
		
	}
	
	public void climb() {
		
	}

}
