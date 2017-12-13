package org.usfirst.frc.team192.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import org.usfirst.frc.team192.strafe.Strafe;
import org.usfirst.frc.team192.swerve.WheelDriveThread;
import org.usfirst.frc.team192.swerve.WheelReadThread;
import org.usfirst.frc.team192.swerve.WheelRotateThread;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {

	private Teleop teleop;
	private CANTalon[] talons;
	private Joystick joystick;
	
	private WheelReadThread[] wheelReads = new WheelReadThread[4];
	private WheelDriveThread[] wheelDrives = new WheelDriveThread[4];
	private WheelRotateThread[] wheelRotates = new WheelRotateThread[4];
	private double[] switchLocs = new double[4];
	
	private Strafe strafe;

	/**
	 * This function is run when the robot is first started up and should be used
	 * for any initialization code.
	 */
	@Override
	public void robotInit() {
		teleop = new Teleop();
		chooser.addDefault("Default Auto", defaultAuto);
		chooser.addObject("My Auto", customAuto);
		SmartDashboard.putData("Auto choices", chooser);

		joystick = new Joystick(0);

		talons = new CANTalon[16];
		for (int i = 0; i < 16; i++)
			talons[i] = new CANTalon(i + 1);

		wheelReads[0] = null; // new WheelReadThread(talons[6], new DigitalInput(3));
		// wheelReads[0].start();
		wheelReads[1] = new WheelReadThread(talons[0], new DigitalInput(1));
		wheelReads[1].start();
		wheelReads[2] = null; // new WheelReadThread(talons[8], new DigitalInput(0));
		// wheelReads[2].start();
		wheelReads[3] = null; // new WheelReadThread(talons[14], new DigitalInput(2));
		// wheelReads[3].start();

		wheelDrives[0] = null; // new WheelDriveThread(talons[7]);
		// wheelDrives[0].start();
		wheelDrives[1] = new WheelDriveThread(talons[1]);
		wheelDrives[1].start();
		wheelDrives[2] = new WheelDriveThread(talons[9]);
		// wheelDrives[2].start();
		wheelDrives[3] = new WheelDriveThread(talons[15]);
		// wheelDrives[3].start();

		wheelRotates[0] = null; // new WheelRotateThread(talons[6], wheelReads[0]);
		// wheelRotates[0].start();
		wheelRotates[1] = new WheelRotateThread(talons[0], wheelReads[1]);
		wheelRotates[1].start();
		wheelRotates[2] = null; // new WheelRotateThread(talons[8], wheelReads[2]);
		// wheelRotates[2].start();
		wheelRotates[3] = null; // new WheelRotateThread(talons[14], wheelReads[3]);
		// wheelRotates[3].start();
		
		strafe = new Strafe(wheelDrives[0], wheelRotates[0], wheelDrives[1], wheelRotates[1], wheelDrives[2], wheelRotates[2], wheelDrives[3], wheelRotates[3]);

	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable chooser
	 * code works with the Java SmartDashboard. If you prefer the LabVIEW Dashboard,
	 * remove all of the chooser code and uncomment the getString line to get the
	 * auto name from the text box below the Gyro
	 *
	 * You can add additional auto modes by adding additional comparisons to the
	 * switch structure below with additional strings. If using the SendableChooser
	 * make sure to add them to the chooser code above as well.
	 */
	@Override
	public void autonomousInit() {
	}

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {
	}

	@Override
	public void teleopInit() {
		teleop.init();
	}

	/**
	 * This function is called periodically during operator control
	 */
	@Override
	public void teleopPeriodic() {
		teleop.periodic();
		/*
		CANTalon talon = talons[6];
		if (joystick.getRawButton(2)) {
			System.out.println("button 2 pressed");
			talon.set(0.25);
		}
		if (joystick.getRawButton(3)) {
			talon.set(-0.25);
			System.out.println("button 3 pressed");
		}
		System.out.println(wheelRead1.getTheta() + " " + talons[6].getEncPosition());
		*/
		
		JoystickInput input = new JoystickInput(1, 2);
		strafe.updateWithJoystickInput(input);
		
		
	}

	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {
	}
}
