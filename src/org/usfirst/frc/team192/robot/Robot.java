package org.usfirst.frc.team192.robot;

import org.usfirst.frc.team192.swerve.experimental.Strafe;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	final String defaultAuto = "Default";
	final String customAuto = "My Auto";
	String autoSelected;
	SendableChooser<String> chooser = new SendableChooser<>();

	private Teleop teleop;

	private TalonSRX testTalon;
	private JoystickInput input;
	
	private double ROBOT_WIDTH = 0.8128;
	private double ROBOT_HEIGHT = 0.7112;

	private Strafe strafe;

	/**
	 * This function is run when the robot is first started up and should be used
	 * for any initialization code.
	 */
	@Override
	public void robotInit() {
		strafe = new Strafe(ROBOT_WIDTH, ROBOT_HEIGHT);
		input = new JoystickInput(0, 1);
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
		strafe.enable();
	}

	/**
	 * This function is called periodically during operator control
	 */
	@Override
	public void teleopPeriodic() {
		strafe.update(input);

	}

	@Override
	public void disabledInit() {
		strafe.disable();
	}

	@Override
	public void testInit() {
		testTalon = new TalonSRX(16);
		testTalon.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 0);
	}

	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {
		testTalon.set(ControlMode.PercentOutput, -input.getTurnStickY());
		System.out.println(testTalon.getSelectedSensorPosition(0));
	}
}
