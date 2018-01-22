package org.usfirst.frc.team192.robot;

import org.usfirst.frc.team192.robot.JoystickInput;
import org.usfirst.frc.team192.vision.Vision;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import java.awt.Point;

import org.usfirst.frc.team192.mechs.*;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.XboxController;

public class Teleop {
	private XboxController xbox;
	private Linkage linkage;
	private Climber climber;
	private Vision vision;
	private Boolean is_vision_toggled;
	private Point centroid;
	private PIDController pid;

	public Teleop(JoystickInput input) {
		xbox = input.getXboxController();
		linkage = new Linkage(new TalonSRX(1));
		climber = new Climber(new TalonSRX(8));
		is_vision_toggled = false;
		centroid = new Point();
		vision = new Vision();
		init();
		
		double P = 0.02;
		double I = 0.0001;
		double D = 0.5;
		double f = 0.0;
		
//		pid = new PIDController(P, I, D, f, gyro, this);
//		pid.setContinuous();
//		pid.setInputRange(0.0, 360.0);
//		pid.setAbsoluteTolerance(3.0);
//		pid.setOutputRange(-1.0, 1.0);
//		pid.reset();
//		pid.setSetpoint(0.0);

	}
	
	public void init() {
		//start swerve thread
		//start vision thread
		vision.cameraThread();
	}
	
	public void periodic() {
		if (xbox.getStartButton()) {
			visionToggleOn();
		}
		else if (xbox.getBackButtonPressed()) {
			visionToggleOff();
		}
		else if(xbox.getAButtonPressed()) {
			switchLinkagePlacement();
		}
		else if(xbox.getBButtonPressed()) {
			scaleLinkagePlacement();
		}
		else if(xbox.getYButtonPressed()) {
			groundLinkagePlacement();
		}else if(xbox.getXButtonPressed()) {
			climb();
		}else if(xbox.getXButtonReleased()) {
			stopClimb();
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
	
	public void intake() {
		
	}
	
	public void reverseIntake() {
		
	}
	
	public void climb() {
		climber.climb();
	}
	
	public void stopClimb() {
		climber.stopClimb();
	}
	
	public void blockVisionPickup() {
		
		
	}
	
	public void exchangeVision() {
		
	}

}
