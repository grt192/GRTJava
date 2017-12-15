package org.usfirst.frc.team192.robot;

import org.usfirst.frc.team192.strafe.Strafe;
import org.usfirst.frc.team192.swerve.WheelDriveThread;
import org.usfirst.frc.team192.swerve.WheelReadThread;
import org.usfirst.frc.team192.swerve.WheelRotateThread;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Teleop {

	private JoystickInput joysticks;
	private CANTalon motor;
	
	final String defaultAuto = "Default";
	final String customAuto = "My Auto";
	String autoSelected;
	SendableChooser<String> chooser = new SendableChooser<>();

	private CANTalon[] talons;
	private Joystick joystick;
	
	private WheelReadThread wheelReads;
	private WheelDriveThread[] wheelDrives = new WheelDriveThread[4];
	private WheelRotateThread wheelRotates;
	// private double[] switchLocs = new double[4];
	private double[] zeroedValue;
	private boolean settingZeros;
	boolean pressingButtonFour = false;
	
	private double WIDTH = 0.7112;
	private double HEIGHT = 0.8128;
	
	private Strafe strafe;
	
	public Teleop() {
		joysticks = new JoystickInput(0, 1);
		motor = new CANTalon(12); //it might be 11
		chooser.addDefault("Default Auto", defaultAuto);
		chooser.addObject("My Auto", customAuto);
		SmartDashboard.putData("Auto choices", chooser);

		joystick = new Joystick(0);

		talons = new CANTalon[16];
		for (int i = 0; i < 16; i++)
			talons[i] = new CANTalon(i + 1);

		wheelReads = new WheelReadThread(talons[6], new DigitalInput(3));

		wheelDrives[0] = new WheelDriveThread(talons[7]);
		wheelDrives[0].start();
		wheelDrives[1] = new WheelDriveThread(talons[1]);
		wheelDrives[1].start();
		wheelDrives[2] = new WheelDriveThread(talons[9]);
		wheelDrives[2].start();
		wheelDrives[3] = new WheelDriveThread(talons[15]);
		wheelDrives[3].start();
		
		zeroedValue = new double[] {0, 0, 0, 0};
		
		wheelRotates = new WheelRotateThread(talons[6], talons[0], talons[8], talons[14], wheelReads);
		
		strafe = new Strafe(wheelRotates, wheelDrives[0], wheelDrives[1], wheelDrives[2], wheelDrives[3], WIDTH, HEIGHT);
		
		System.out.println("zeroing all wheels");
	}

	public void init() {
		
		wheelRotates.setTargetTheta(0);
		wheelReads.zero();
		
		/*
		for (int i = 0; i < wheelRotates.length; i++) {
			wheelRotates[i].shouldStillRun = false;
			wheelDrives[i].shouldStillRun = false;
		}
		
		for (int i = 0; i < wheelRotates.length; i++) {
			wheelRotates[i].setTargetTheta(0);
			System.out.println(i + ": " + wheelReads[i].getTheta());
			wheelRotates[i].setReadingWhenZeroed(wheelReads[i].getTheta());
			wheelRotates[i].shouldStillRun = true;
			wheelDrives[i].shouldStillRun = true;
		}
		*/
	}

	public void periodic() {
		doSwerve();
		motor.set(joysticks.getClippedY(Hand.kLeft));
	}

	private void doSwerve() {
		
		if (joystick.getRawButton(2)) {
			strafe.changeModeToRotate();
		}
		if (joystick.getRawButton(3)) {
			strafe.changeModeToStrafe();
		}
		/*
		if (joystick.getRawButton(4)) {
			pressingButtonFour = true;
		} else if (pressingButtonFour) {
			if (settingZeros) {
				for (int i = 0; i < wheelRotates.length; i++) {
					wheelRotates[i].setTargetTheta(0);
					System.out.println(i + ": " + wheelReads[i].getTheta());
					wheelRotates[i].setReadingWhenZeroed(wheelReads[i].getTheta());
					wheelRotates[i].shouldStillRun = true;
					wheelDrives[i].shouldStillRun = true;
				}
				settingZeros = false;
			} else {
				for (int i = 0; i < wheelRotates.length; i++) {
					wheelRotates[i].shouldStillRun = false;
					wheelDrives[i].shouldStillRun = false;
				}
				settingZeros = true;
			}
			pressingButtonFour = false;
			
		}
		*/
		
		strafe.updateWithJoystickInput(joysticks);
	}
}













